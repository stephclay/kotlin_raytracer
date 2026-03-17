package com.wombatsw.raytracing.engine

import com.wombatsw.raytracing.EPSILON
import com.wombatsw.raytracing.math.MathUtils.randomDouble
import com.wombatsw.raytracing.model.*

private val INITIAL_T_RANGE = Interval(EPSILON, Double.POSITIVE_INFINITY)

/**
 * Scene renderer
 *
 * @param[scene] The scene to render
 * @param[antiAliasingSamples] The number of samples per pixel
 * @property[maxDepth] The maximum depth for bouncing rays
 */
class Renderer(scene: Scene, antiAliasingSamples: Int, val maxDepth: Int) {
    private val camera = scene.camera
    private val world: Node = BVHNode(scene.world)
    private val antiAliasOffsets: List<Vector> = List(antiAliasingSamples) {
        Vector(0, 0, 0) + camera.pixelDU * (randomDouble() - 0.5) +
                camera.pixelDV * (randomDouble() - 0.5)
    }

    /**
     * Render the scene into a raster image
     *
     * @return a [ByteArray] containing the output image
     */
    fun render(): ByteArray {
        val imageData = ByteArray(camera.imageWidth * camera.imageHeight * 3)

        for (j in 0..<camera.imageHeight) {
            val rowIndex = j * camera.imageWidth

            for (i in 0..<camera.imageWidth) {
                val offset = (rowIndex + i) * 3
                writeColor(getColor(i, j), imageData, offset)
            }
        }
        return imageData
    }

    /**
     * Write the specified [Color] to the output array
     *
     * @param[color] The color of the pixel
     * @param[data] The output array
     * @param[offset] The index of the output color triplet in raster order
     */
    private fun writeColor(color: Color, data: ByteArray, offset: Int) {
        data[offset] = color.redByte()
        data[offset + 1] = color.greenByte()
        data[offset + 2] = color.blueByte()
    }

    /**
     * Get the color of the specified pixel
     *
     * @param[i] the horizontal pixel coordinate
     * @param[j] the vertical pixel coordinate
     * @return the [Color] of the pixel
     */
    private fun getColor(i: Int, j: Int): Color =
        Color.average(
            getSamplingPoints(i, j)
                .map(camera::rayForPoint)
                .map { ray -> getColor(ray, maxDepth) }
        )

    /**
     * Get the color of the specified [Ray]
     *
     * @param[ray] the incoming ray
     * @param[depth] how many times can the ray bounce off of objects
     * @return the [Color] of the ray
     */
    private fun getColor(ray: Ray, depth: Int): Color {
        if (depth <= 0) {
            return Color(0, 0, 0)
        }

        val (intersection, material) = world.intersect(ray, INITIAL_T_RANGE) ?: return camera.background

        val emitted = material.emitted(intersection)
        val scatterData = material.scatter(intersection) ?: return emitted

        val scatterColor = getColor(scatterData.ray, depth - 1)
        return scatterColor * scatterData.attenuation + emitted
    }

    /**
     * Get the list of anti-aliasing sample points
     *
     * @param[x] the x coordinate of the resulting image
     * @param[y] the y coordinate of the resulting image
     * @return the list of sampling points
     */
    private fun getSamplingPoints(x: Int, y: Int): List<Point> {
        val u = camera.pixelDU * x
        val v = camera.pixelDV * y
        return antiAliasOffsets.map { offset -> camera.pixelOrigin + u + v + offset }
    }
}
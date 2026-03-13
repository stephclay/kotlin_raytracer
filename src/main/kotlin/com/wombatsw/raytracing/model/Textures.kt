package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.EPSILON
import com.wombatsw.raytracing.IMG_MAP_INTERVAL
import com.wombatsw.raytracing.math.Perlin
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.floor
import kotlin.math.sin

/**
 * Texture base class
 */
sealed class Texture {
    abstract fun value(p: Point, u: Double, v: Double): Color
}

/**
 * Solid color texture
 *
 * @property[color] The texture color
 */
data class SolidColor(val color: Color) : Texture() {
    override fun value(p: Point, u: Double, v: Double): Color = color
}

/**
 * Checker Texture
 *
 * @property[scale] The scaling factor for the texture
 * @property[even] The even texture
 * @property[odd] The odd texture
 */
data class CheckerTexture(val scale: Double, val even: Texture, val odd: Texture) : Texture() {
    val invScale = 1.0 / scale

    override fun value(p: Point, u: Double, v: Double): Color {
        val i = floor(invScale * p.x).toInt() +
                floor(invScale * p.y).toInt() +
                floor(invScale * p.z).toInt()
        return if (i % 2 == 0) {
            even.value(p, u, v)
        } else {
            odd.value(p, u, v)
        }
    }
}

/**
 * Image texture
 *
 * @property[filename] The image to map onto a surface
 */
data class ImageTexture(val filename: String) : Texture() {

    private val image: BufferedImage

    init {
        val file = File(filename)
        val stream = if (file.exists()) file.inputStream() else javaClass.getResourceAsStream("/$filename")
        require(stream != null) { "File $filename not found" }

        val img = ImageIO.read(stream)
        require(img != null) { "Image $filename cannot be read" }
        image = img
    }

    override fun value(p: Point, u: Double, v: Double): Color {
        val x = (IMG_MAP_INTERVAL.clamp(u) * image.width).toInt()
        val y = ((1.0 - EPSILON - IMG_MAP_INTERVAL.clamp(v)) * image.height).toInt()
        val color = image.getRGB(x, y)

        return Color(
            getColorComponent(color, 16),
            getColorComponent(color, 8),
            getColorComponent(color, 0)
        )
    }

    private fun getColorComponent(value: Int, shift: Int): Double =
        ((value shr shift) and 0xff) / 255.00
}

/**
 * Noise texture
 *
 * @property[scale] The scaling factor for the noise field
 */
data class NoiseTexture(val scale: Double) : Texture() {
    private val perlin = Perlin()

    override fun value(p: Point, u: Double, v: Double): Color {
        val adj = (1 + sin(scale * p.z + 10 * perlin.turbulence(p, 7)))
        return Color(0.5, 0.5, 0.5) * adj
    }
}

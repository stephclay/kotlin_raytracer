package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.math.RandomUtils.Companion.randomInUnitDisc
import kotlin.math.tan

/**
 * Camera data
 *
 * @property[imageWidth] Image width
 * @property[imageHeight] Image height
 * @property[background] Background color
 * @property[cameraCenter] Camera center
 * @property[defocusAngle] The defocusing angle in degrees
 * @property[focusDistance] The focus distance
 * @property[fieldOfView] The field of view in degrees
 * @property[viewportCenter] The viewport center
 * @property[viewUp] The viewport "up" direction
 */
data class Camera(
    val imageWidth: Int,
    val imageHeight: Int,
    val background: Color,
    val cameraCenter: Point,
    val defocusAngle: Double,
    val focusDistance: Double,
    val fieldOfView: Double,
    val viewportCenter: Point,
    val viewUp: Vector
) {
    /** @return the vector measuring 1 pixel in the horizontal direction */
    val pixelDU: Vector

    /** @return the vector measuring 1 pixel in the vertical direction */
    val pixelDV: Vector

    /** @return the centerpoint of the upper left pixel */
    val pixelOrigin: Point

    private val defocusDiscU: Vector
    private val defocusDiscV: Vector

    init {
        val h = tan(Math.toRadians(fieldOfView) / 2.0)
        val viewportHeight = 2.0 * h * focusDistance
        val viewportWidth = viewportHeight * imageWidth.toDouble() / imageHeight.toDouble()

        val viewBasisW = (cameraCenter - viewportCenter).normalize()
        val viewBasisU = viewUp.cross(viewBasisW).normalize()
        val viewBasisV = viewBasisW.cross(viewBasisU).normalize()

        val viewportU = viewBasisU * viewportWidth
        val viewportV = viewBasisV * -viewportHeight

        pixelDU = viewportU / imageWidth
        pixelDV = viewportV / imageHeight

        val viewportUpperLeft = cameraCenter +
                (viewBasisW * -focusDistance) -
                (viewportU / 2) - (viewportV / 2)

        pixelOrigin = viewportUpperLeft + pixelDU / 2 + pixelDV / 2

        val defocusRadius = focusDistance * tan(Math.toRadians(defocusAngle) / 2.0)
        defocusDiscU = viewBasisU * defocusRadius
        defocusDiscV = viewBasisV * -defocusRadius
    }

    /**
     * Get the ray pointing from the camera to the specified location on the viewport. If defocus is enabled,
     * the ray will be deflected accordingly
     *
     * @param[point] The point on the viewport
     * @return The ray
     */
    fun rayForPoint(point: Point): Ray {
        val rayOrigin = if (defocusAngle <= 0) {
            cameraCenter
        } else {
            val v = randomInUnitDisc()
            cameraCenter + defocusDiscU * v.first + defocusDiscV * v.second
        }
        return Ray(rayOrigin, point - rayOrigin)
    }
}
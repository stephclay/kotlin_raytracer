package com.wombatsw.raytracing.model

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
)
package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Camera
import com.wombatsw.raytracing.scene.InlineRef
import com.wombatsw.raytracing.scene.Ref
import com.wombatsw.raytracing.scene.ResolutionContext
import com.wombatsw.raytracing.scene.Resolvable

/**
 * Camera DTO
 *
 * @property[imageWidth] Image width. Defaults to 100
 * @property[imageHeight] Image height. Defaults to 100
 * @property[background] Background color [Ref]. Defaults to black
 * @property[cameraCenter] Camera center [Ref]. Defaults to the origin (0, 0, 0)
 * @property[defocusAngle] The defocusing angle in degrees. Defaults to none (0 degrees)
 * @property[focusDistance] The focus distance. Defaults to 10 (world units)
 * @property[fieldOfView] The field of view in degrees. Defaults to 90 degrees
 * @property[viewportCenter] The viewport center [Ref]. Defaults to "-1 in the Z direction" (0, 0, -1)
 * @property[viewUp] The viewport "up" direction [Ref]. Defaults to "y up" (0, 1, 0)
 */
data class CameraDTO(
    val imageWidth: Int = 100,
    val imageHeight: Int = 100,
    val background: Ref<TripletDTO> = InlineRef(TripletDTO(listOf(0.0, 0.0, 0.0))),
    val cameraCenter: Ref<TripletDTO> = InlineRef(TripletDTO(listOf(0.0, 0.0, 0.0))),
    val defocusAngle: Double = 0.0,
    val focusDistance: Double = 10.0,
    val fieldOfView: Double = 90.0,
    val viewportCenter: Ref<TripletDTO> = InlineRef(TripletDTO(listOf(0.0, 0.0, -1.0))),
    val viewUp: Ref<TripletDTO> = InlineRef(TripletDTO(listOf(0.0, 1.0, 0.0)))
) : Resolvable<Camera> {
    override fun resolve(ctx: ResolutionContext): Camera =
        Camera(
            imageWidth,
            imageHeight,
            ctx.resolveColor(background),
            ctx.resolvePoint(cameraCenter),
            defocusAngle,
            focusDistance,
            fieldOfView,
            ctx.resolvePoint(viewportCenter),
            ctx.resolveVector(viewUp)
        )
}
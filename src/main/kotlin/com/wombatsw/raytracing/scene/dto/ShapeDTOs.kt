package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Box
import com.wombatsw.raytracing.model.Node
import com.wombatsw.raytracing.model.Quad
import com.wombatsw.raytracing.model.Sphere
import com.wombatsw.raytracing.scene.Ref
import com.wombatsw.raytracing.scene.ResolutionContext
import com.wombatsw.raytracing.scene.Resolvable

/**
 * Shape DTO base class
 *
 * @property[material] The material [Ref] that the shape is made of
 */
sealed class ShapeDTO(open val material: Ref<MaterialDTO>) : Resolvable<Node>

/**
 * Sphere DTO
 *
 * @property[center] The center [Ref] of the sphere
 * @property[radius] The radius of the sphere
 * @property[material] The material [Ref] that the shape is made of
 */
data class SphereDTO(val center: Ref<TripletDTO>, val radius: Double, override val material: Ref<MaterialDTO>) :
    ShapeDTO(material) {
    override fun resolve(ctx: ResolutionContext): Sphere =
        Sphere(ctx.resolvePoint(center), radius, ctx.resolveMaterial(material))
}

/**
 * Quadrilateral polygon DTO
 *
 * @param[corner] One corner [Ref] of the quad
 * @param[u] First vector [Ref] to adjacent vertex of q
 * @param[v] Second vector [Ref] to adjacent vertex of q
 * @property[material] The material [Ref] that the shape is made of
 */
data class QuadDTO(
    val corner: Ref<TripletDTO>, val u: Ref<TripletDTO>, val v: Ref<TripletDTO>,
    override val material: Ref<MaterialDTO>
) :
    ShapeDTO(material) {
    override fun resolve(ctx: ResolutionContext): Quad =
        Quad(
            ctx.resolvePoint(corner),
            ctx.resolveVector(u),
            ctx.resolveVector(v),
            ctx.resolveMaterial(material)
        )

}

/**
 * Axis-aligned box DTO
 *
 * @param[corner1] One corner [Ref] of the box
 * @param[corner2] The opposite corner [Ref] of the box
 * @property[material] The material [Ref] that the shape is made of
 */
data class BoxDTO(val corner1: Ref<TripletDTO>, val corner2: Ref<TripletDTO>, override val material: Ref<MaterialDTO>) :
    ShapeDTO(material) {
    override fun resolve(ctx: ResolutionContext): Box {
        return Box(
            ctx.resolvePoint(corner1),
            ctx.resolvePoint(corner2),
            ctx.resolveMaterial(material)
        )
    }
}
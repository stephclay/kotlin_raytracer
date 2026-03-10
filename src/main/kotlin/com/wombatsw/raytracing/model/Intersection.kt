package com.wombatsw.raytracing.model

/**
 * Data encapsulating an intersection with an object
 *
 * @property[ray] The intersecting [Ray]
 * @property[t] The distance along the ray
 * @property[p] The [Point] of intersection
 * @param[outerN] The outward-facing normal [Vector] at the point of intersection
 * @property[u] The 2D "horizontal" coordinate on the surface at the point of intersection
 * @property[v] The 2D "vertical" coordinate on the surface at the point of intersection
 */
data class Intersection(
    val ray: Ray,
    val t: Double,
    val p: Point,
    private val outerN: Vector,
    val u: Double,
    val v: Double
) {
    /**
     * Whether the intersaction was on the front face (outside) or back face (inside) of the object
     */
    val frontFace: Boolean = ray.direction.dot(outerN) < 0

    /**
     * The normal at the point of intersection, which may point into the object
     */
    val n: Vector = if (frontFace) outerN else -outerN
}
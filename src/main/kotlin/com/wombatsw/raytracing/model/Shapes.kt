package com.wombatsw.raytracing.model

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * Shape base class
 *
 * @property[material] The material that the shape is made of
 */
sealed class Shape(open val material: Material) : Node

/**
 * A sphere
 *
 * @property[center] The center of the sphere
 * @property[radius] The radius of the sphere
 * @property[material] The material that the shape is made of
 */
data class Sphere(val center: Point, val radius: Double, override val material: Material) : Shape(material) {
    private val bbox: BoundingBox
    override fun boundingBox() = bbox

    init {
        val rVec = Vector(radius, radius, radius)
        bbox = BoundingBox(center - rVec, center + rVec)
    }

    override fun intersect(ray: Ray, tRange: Interval): Pair<Intersection, Material>? {
        val oc = center - ray.origin
        val a = ray.direction.dot(ray.direction)
        val h = ray.direction.dot(oc)
        val c = oc.dot(oc) - radius * radius

        val discriminant = h * h - a * c
        if (discriminant < 0) {
            return null
        }
        val sqrtD = sqrt(discriminant)

        val t1 = (h - sqrtD) / a
        if (tRange.contains(t1)) {
            return getIntersection(center, ray, t1)
        }

        val t2 = (h + sqrtD) / a
        if (tRange.contains(t2)) {
            return getIntersection(center, ray, t2)
        }

        return null
    }

    /**
     * Get the intersection data for the given center, ray, and t-value
     *
     * @param[center] The sphere's center
     * @param[ray] The incoming [Ray]
     * @param[t] The t-value at the intersection point
     * @return A pair containing the [Intersection] and [Material] data
     */
    private fun getIntersection(center: Point, ray: Ray, t: Double): Pair<Intersection, Material> {
        val p: Point = ray.at(t)
        val n: Vector = (p - center) / radius

        val u = (atan2(-n.z, n.x) + PI) / (2.0 * PI)
        val v = acos(-n.y) / PI

        return Pair(Intersection(ray, t, p, n, u, v), material)
    }
}

package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.EPSILON
import kotlin.math.*

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

/**
 * Quadrilateral polygon
 *
 * @param[corner] One corner of the quad
 * @param[u] First vector to adjacent vertex of q
 * @param[v] Second vector to adjacent vertex of q
 * @param material Material quad is made of
 */
data class Quad(val corner: Point, val u: Vector, val v: Vector, override val material: Material) : Shape(material) {
    private val bbox: BoundingBox
    private val n: Vector
    private val w: Vector
    private val d: Double

    override fun boundingBox() = bbox

    init {
        val bbDiag1 = BoundingBox(corner, corner + u + v)
        val bbDiag2 = BoundingBox(corner + u, corner + v)
        bbox = bbDiag1 + bbDiag2

        val norm = u.cross(v)
        n = norm.normalize()
        w = norm / norm.lengthSquared()
        d = n.dot(corner)
    }

    override fun intersect(ray: Ray, tRange: Interval): Pair<Intersection, Material>? {
        val nd = n.dot(ray.direction)

        // If the ray is parallel to the plane, then no intersection
        if (abs(nd) < EPSILON) {
            return null
        }

        // Return if plane intersection is out of range
        val t = (d - ray.origin.dot(n)) / nd
        if (!tRange.contains(t)) {
            return null
        }

        val p = ray.at(t)
        val planarHit = p - corner
        val a = w.dot(planarHit.cross(v))
        val b = w.dot(u.cross(planarHit))

        if (!UNIT_INTERVAL.contains(a) || !UNIT_INTERVAL.contains(b)) {
            return null
        }

        return Pair(Intersection(ray, t, p, n, a, b), material)
    }
}

/**
 * Axis-aligned box
 *
 * @param[corner1] One corner of the box
 * @param[corner2] The opposite corner of the box
 * @property[material] The material that the shape is made of
 */
data class Box(val corner1: Point, val corner2: Point, override val material: Material) : Shape(material) {
    private val faces: BVHNode

    init {
        val minX = min(corner1.x, corner2.x)
        val minY = min(corner1.y, corner2.y)
        val minZ = min(corner1.z, corner2.z)
        val maxX = max(corner1.x, corner2.x)
        val maxY = max(corner1.y, corner2.y)
        val maxZ = max(corner1.z, corner2.z)

        val dx = Vector(maxX - minX, 0, 0)
        val dy = Vector(0, maxY - minY, 0)
        val dz = Vector(0, 0, maxZ - minZ)

        faces = BVHNode(
            listOf(
                Quad(Point(minX, minY, maxZ), dx, dy, material),  // Front
                Quad(Point(maxX, minY, maxZ), -dz, dy, material), // Right
                Quad(Point(maxX, minY, minZ), -dx, dy, material), // Back
                Quad(Point(minX, minY, minZ), dz, dy, material),  // Left
                Quad(Point(minX, maxY, maxZ), dx, -dz, material), // Top
                Quad(Point(minX, minY, minZ), dx, dz, material)   // Bottom
            )
        )
    }

    override fun boundingBox(): BoundingBox = faces.boundingBox()
    override fun intersect(ray: Ray, tRange: Interval): Pair<Intersection, Material>? = faces.intersect(ray, tRange)
}
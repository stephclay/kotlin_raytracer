package com.wombatsw.raytracing.model

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
}

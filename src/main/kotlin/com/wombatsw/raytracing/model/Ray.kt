package com.wombatsw.raytracing.model

/**
 * A Ray consisting of a point of origin and direction
 *
 * @property[origin] The starting point of the ray
 * @property[direction] The direction the ray is pointing
 */
data class Ray(val origin: Point, val direction: Vector) {
    /**
     * Find a point at a specific distance along the ray. One unit distance is measured by the length of
     * the [direction] vector.
     *
     * @param[t] The distance along the ray
     * @return The point at position [t] along the ray
     */
    fun at(t: Number): Point = origin + direction * t
}
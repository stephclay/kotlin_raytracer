package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.EPSILON
import com.wombatsw.raytracing.model.Interval.Companion.EMPTY
import kotlin.math.max
import kotlin.math.min

/**
 * Axis-aligned bounding box to help optimize ray intersection logic. The box is always aligned along world
 * coordinates to make the intersection logic much faster.
 *
 * @property[x] The X-axis bounds of the box
 * @property[y] The Y-axis bounds of the box
 * @property[z] The Z-axis bounds of the box
 */
data class BoundingBox(val x: Interval, val y: Interval, val z: Interval) {
    constructor() : this(EMPTY, EMPTY, EMPTY)
    constructor(a: Point, b: Point) : this(
        padToMin(Interval.createOrdered(a.x, b.x)),
        padToMin(Interval.createOrdered(a.y, b.y)),
        padToMin(Interval.createOrdered(a.z, b.z))
    )

    /**
     * Create a new bounding box which can contain this one and the specified one
     *
     * @param[other] The other bounding box
     * @return The new [BoundingBox]
     */
    operator fun plus(other: BoundingBox): BoundingBox = BoundingBox(
        padToMin(x + other.x),
        padToMin(y + other.y),
        padToMin(z + other.z)
    )

    /**
     * Check if the given ray intersects this bounding box somewhere within the provided range
     *
     * @param[ray] The incoming ray
     * @param[tRange] The acceptable t values for an intersection
     * @return A new interval containing the t-values which are within the bounding box, or null if the
     *         ray did not intersect
     */
    fun intersect(ray: Ray, tRange: Interval): Interval? {
        var min = tRange.min
        var max = tRange.max
        for (axis in 0..2) {
            val axisInterval = axisInterval(axis)
            val dirInv = 1.0 / axisValue(ray.direction, axis)

            val t0 = (axisInterval.min - axisValue(ray.origin, axis)) * dirInv
            val t1 = (axisInterval.max - axisValue(ray.origin, axis)) * dirInv
            if (t0 < t1) {
                min = max(t0, min)
                max = min(t1, max)
            } else {
                min = max(t1, min)
                max = min(t0, max)
            }

            if (max <= min) {
                return null
            }
        }
        return Interval(min, max)
    }

    /**
     * return the vector component for the given index
     *
     * @param[v] The vector
     * @param[index] The axis index, where 0 = X, 1 = Y, 2 = Z
     * @return The component value
     */
    private fun axisValue(v: Vector, index: Int): Double =
        when (index) {
            0 -> v.x
            1 -> v.y
            2 -> v.z
            else -> throw IllegalStateException("Invalid axis: $index")
        }

    /**
     * Get the interval for the indicated axis
     *
     * @param[index] The interval index where 0 = X, 1 = Y, and 2 = Z. Other values are invalid
     * @return The indicated [Interval]
     */
    fun axisInterval(index: Int): Interval =
        when (index) {
            0 -> x
            1 -> y
            2 -> z
            else -> error("Invalid axis: $index")
        }

    /**
     * Get the index of the longest axis interval
     *
     * @return The index of the longest interval
     */
    fun longestAxis(): Int {
        return if (x.size() > y.size()) {
            if (x.size() > z.size()) 0 else 2
        } else {
            if (y.size() > z.size()) 1 else 2
        }
    }

    companion object {
        /**
         * Helper method to make sure that the interval has a minimum size.
         *
         * @param interval The original interval
         * @return The interval with a minimum size indicated by [EPSILON]
         */
        fun padToMin(interval: Interval) =
            if (interval.size() < EPSILON) interval.expand(EPSILON) else interval
    }
}

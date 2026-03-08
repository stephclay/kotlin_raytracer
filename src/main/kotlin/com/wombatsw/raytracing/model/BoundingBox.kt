package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.EPSILON
import com.wombatsw.raytracing.model.Interval.Companion.EMPTY

/**
 * Bounding Box to help optimize ray intersection logic. The box is always aligned along world coordinates to make
 * the intersection logic much faster.
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

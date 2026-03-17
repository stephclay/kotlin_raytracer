package com.wombatsw.raytracing.model

import kotlin.math.max
import kotlin.math.min

val UNIT_INTERVAL = Interval(0, 1)

/**
 * Interval between 2 numeric values
 *
 * @property[min] The lower bound of the interval
 * @property[max] The upper bound of the interval
 */
data class Interval(val min: Double, val max: Double) {
    constructor(min: Number, max: Number) : this(min.toDouble(), max.toDouble())

    /**
     * @return The size of this [Interval]. May be negative for empty intervals
     */
    fun size() = max - min

    /**
     * Union of intervals. If the intervals do not overlap, then the new interval will contain the space between them
     * as well.
     *
     * @param[other] The other [Interval]
     * @return The new [Interval]
     */
    operator fun plus(other: Interval): Interval =
        Interval(min(min, other.min), max(max, other.max))

    /**
     * Check if the supplied value is in the [Interval] range, inclusive
     *
     * @param[x] The value to check
     * @return whether [x] is contained in this [Interval]
     */
    fun contains(x: Double) = x in min..max

    /**
     * Check if the supplied value is in the [Interval] range, exclusive
     *
     * @param[x] The value to check
     * @return whether [x] is surrounded by this [Interval]
     */
    fun surrounds(x: Double) = min < x && x < max

    /**
     * Clamp the provided value to be within this [Interval]
     *
     * @param[x] the value to clamp
     * @return The clamped value
     */
    fun clamp(x: Double) = x.coerceIn(min, max)

    /**
     * Expand the interval at each end by the provided delta
     *
     * @param[delta] The value with which to expand the interval
     * @return The expanded [Interval]
     */
    fun expand(delta: Double): Interval {
        val pad = delta / 2.0
        return Interval(min - pad, max + pad)
    }

    companion object {
        /**
         * The empty interval
         */
        val EMPTY = Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)

        /**
         * Create a new [Interval] using the provided bounds. Will be ordered to make sure that the lower bound is less
         * than or equal to the upper bound.
         *
         * @param[v0] One bound of the [Interval]
         * @param[v1] The other bound of the [Interval]
         * @return The new [Interval]
         */
        fun createOrdered(v0: Double, v1: Double): Interval =
            if (v0 <= v1) Interval(v0, v1) else Interval(v1, v0)
    }
}
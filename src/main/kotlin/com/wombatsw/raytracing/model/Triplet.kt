package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.math.RandomUtils.Companion.randomDouble

typealias Point = Triplet
typealias Vector = Triplet
typealias Color = Triplet

/**
 * Common class for points, vectors, and colors
 */
class Triplet(val x: Double, val y: Double, val z: Double) {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun plus(a: Triplet): Triplet = Triplet(x + a.x, y + a.y, z + a.z)
    operator fun minus(a: Triplet): Triplet = Triplet(x - a.x, y - a.y, z - a.z)
    operator fun times(a: Double): Triplet = Triplet(x * a, y * a, z * a)
    operator fun times(a: Number): Triplet = times(a.toDouble())
    operator fun unaryMinus() = Triplet(-x, -y, -z)

    /**
     * Take the dot product with supplied triplet
     *
     * @param[a] The other triplet
     * @return The dot product
     */
    fun dot(a: Triplet) = x * a.x + y * a.y + z * a.z

    /**
     * Take the cross product with supplied triplet
     *
     * @param[a] The other triplet
     * @return The cross product
     */
    fun cross(a: Triplet) = Triplet(
        y * a.z - z * a.y,
        z * a.x - x * a.z,
        x * a.y - y * a.x
    )

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Triplet) return false
        return this.x == other.x && this.y == other.y && this.z == other.z
    }

    override fun hashCode(): Int {
        return x.hashCode() * 509 + y.hashCode() * 37 + z.hashCode()
    }

    companion object {
        fun random(min: Number, max: Number): Triplet =
            Triplet(
                randomDouble(min, max),
                randomDouble(min, max),
                randomDouble(min, max)
            )
    }
}

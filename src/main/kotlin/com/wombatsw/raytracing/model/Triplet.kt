package com.wombatsw.raytracing.model

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
}

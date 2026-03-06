package com.wombatsw.raytracing.model

typealias Point = Triplet
typealias Vector = Triplet
typealias Color = Triplet

/**
 * Common class for points, vectors, and colors
 */
class Triplet(val x: Double, val y: Double, val z: Double) {

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

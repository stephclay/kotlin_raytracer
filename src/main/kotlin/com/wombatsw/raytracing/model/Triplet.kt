package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.EPSILON
import com.wombatsw.raytracing.math.RandomUtils.Companion.randomDouble
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

typealias Point = Triplet
typealias Vector = Triplet
typealias Color = Triplet

val BLACK = Color(0, 0, 0)
val WHITE = Color(1, 1, 1)

operator fun Number.times(a: Triplet): Triplet {
    return a.times(this)
}

/**
 * Common class for points, vectors, and colors
 */
class Triplet(val x: Double, val y: Double, val z: Double) {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun plus(a: Triplet): Triplet = Triplet(x + a.x, y + a.y, z + a.z)
    operator fun minus(a: Triplet): Triplet = Triplet(x - a.x, y - a.y, z - a.z)
    operator fun times(a: Double): Triplet = Triplet(x * a, y * a, z * a)
    operator fun times(a: Number): Triplet = times(a.toDouble())
    operator fun times(a: Triplet) = Triplet(x * a.x, y * a.y, z * a.z)
    operator fun div(a: Double) = Triplet(x / a, y / a, z / a)
    operator fun div(a: Number) = div(a.toDouble())
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

    fun lengthSquared() = dot(this)
    fun length() = sqrt(lengthSquared())
    fun normalize() = this / length()
    fun nearZero() = abs(x) < EPSILON &&
            abs(y) < EPSILON &&
            abs(z) < EPSILON

    fun reflect(n: Vector): Vector =
        this - n * (2.0 * dot(n))

    fun refract(n: Vector, etaRatio: Double): Vector {
        // Compute the perpendicular component: rPerp = (rOrig + cosTheta * N) * etaRatio
        val cosTheta = min(1.0, -dot(n))
        val rPerp = (this + n * cosTheta) * etaRatio

        // Compute the parallel component: rParallel = -sqrt(|1 - rPerp.rPerp|) * N
        val rParallel = -sqrt(abs(1.0 - rPerp.lengthSquared())) * n
        return rPerp + rParallel
    }

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

        fun randomUnitVector(): Vector {
            while (true) {
                val vector = random(-1, 1)
                val lenSq = vector.lengthSquared()
                if (lenSq in 1e-160..1.0) {
                    return vector.div(sqrt(lenSq))
                }
            }
        }
    }
}

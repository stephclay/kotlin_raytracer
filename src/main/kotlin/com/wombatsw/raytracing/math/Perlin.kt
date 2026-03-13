package com.wombatsw.raytracing.math

import com.wombatsw.raytracing.model.Point
import com.wombatsw.raytracing.model.Vector
import kotlin.math.abs
import kotlin.math.floor

/**
 * Perlin noise generator
 */
class Perlin {
    private val pointCount = 256
    private val randVec = Array(pointCount) { Vector.random(-1, 1) }
    private val permX = IntArray(pointCount) { it }
    private val permY = IntArray(pointCount) { it }
    private val permZ = IntArray(pointCount) { it }

    init {
        permute(permX)
        permute(permY)
        permute(permZ)
    }

    private fun permute(perm: IntArray) {
        for (i in perm.size - 1 downTo 1) {
            val target: Int = RandomUtils.randomInt(0, i)
            val tmp = perm[i]
            perm[i] = perm[target]
            perm[target] = tmp
        }
    }

    /**
     * Generate Perlin noise for a given point
     *
     * @param[p] The point to generate the noise for
     * @return A [Double] containing the noise value
     */
    fun noise(p: Point): Double {
        val u = p.x - floor(p.x)
        val v = p.y - floor(p.y)
        val w = p.z - floor(p.z)

        val i = floor(p.x).toInt()
        val j = floor(p.y).toInt()
        val k = floor(p.z).toInt()
        val uu = u * u * (3 - 2 * u)
        val vv = v * v * (3 - 2 * v)
        val ww = w * w * (3 - 2 * w)

        var accum = 0.0
        for (di in 0..1) {
            for (dj in 0..1) {
                for (dk in 0..1) {
                    val vec = randVec[(permX[(i + di) and 0xff] xor
                            permY[(j + dj) and 0xff] xor
                            permZ[(k + dk) and 0xff])]

                    val weight = Vector(u - di, v - dj, w - dk)
                    accum += vec.dot(weight) *
                            (di * uu + (1 - di) * (1 - uu)) *
                            (dj * vv + (1 - dj) * (1 - vv)) *
                            (dk * ww + (1 - dk) * (1 - ww))
                }
            }
        }
        return accum
    }

    /**
     * Generate Perlin noise with turbulence for a given point
     *
     * @param[p] The point to generate the noise for
     * @param[depth] The depth factor for the turbulence
     * @return A [Double] containing the noise value
     */
    fun turbulence(p: Point, depth: Int): Double {
        var accum = 0.0
        var tempP = p
        var weight = 1.0

        repeat(depth) {
            accum += weight * noise(tempP)
            weight *= 0.5
            tempP *= 2.0
        }

        return abs(accum)
    }
}

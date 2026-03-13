package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TripletTest {
    @Test
    fun `plus should do component level addition`() {
        val t1 = Triplet(x = 1, y = 2, z = 3)
        val t2 = Triplet(x = 4, y = 5, z = 6)

        val result = t1 + t2
        assertEquals(Triplet(5, 7, 9), result)
    }

    @Test
    fun `minus should do component level subtraction`() {
        val t1 = Triplet(x = 1, y = 2, z = 3)
        val t2 = Triplet(x = 4, y = 5, z = 6)

        val result = t1 - t2
        assertEquals(Triplet(-3, -3, -3), result)
    }

    @Test
    fun `times with a scalar should scale the triplet`() {
        val t1 = Triplet(x = 1, y = 2, z = 3)

        val result = t1 * 10
        assertEquals(Triplet(10, 20, 30), result)
    }

    @Test
    fun `unaryMinus should negate the triplet`() {
        val t1 = Triplet(x = 1, y = 2, z = 3)

        val result = -t1
        assertEquals(Triplet(-1, -2, -3), result)
    }

    @Test
    fun `dot should return the dot product`() {
        val t1 = Triplet(x = 1, y = 2, z = 3)
        val t2 = Triplet(x = 4, y = 5, z = -6)

        val result = t1.dot(t2)
        assertEquals(-4.0, result)
    }

    @Test
    fun `dot should return zero for orthogonal vectors`() {
        val t1 = Triplet(x = 1, y = 0, z = 0)
        val t2 = Triplet(x = 0, y = 1, z = 1)

        val result = t1.dot(t2)
        assertEquals(0.0, result)
    }

    @Test
    fun `cross should return the cross product`() {
        val t1 = Triplet(x = 1, y = 0, z = 0)
        val t2 = Triplet(x = 1, y = 1, z = 0)

        val result = t1.cross(t2)
        assertEquals(Triplet(0, 0, 1), result)
    }

    @Test
    fun `cross product of parallel vectors should return a zero vector`() {
        val t1 = Triplet(x = 1, y = 1, z = 0)
        val t2 = Triplet(x = 2, y = 2, z = 0)

        val result = t1.cross(t2)
        assertEquals(Triplet(0, 0, 0), result)
    }

    @Test
    fun `new random triplet should be within provided bounds`() {
        val range = 0.0001..0.0002

        repeat(100) {
            val triplet = Triplet.random(range.start, range.endInclusive)

            assertContains(range, triplet.x)
            assertContains(range, triplet.y)
            assertContains(range, triplet.z)
        }
    }
}
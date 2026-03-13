package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.EPSILON
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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
    fun `times with a triplet should perform a component-wise multiplication`() {
        val t1 = Triplet(x = 1, y = 2, z = 3)
        val t2 = Triplet(x = 4, y = -2, z = 0)

        val result = t1 * t2
        assertEquals(Triplet(4, -4, 0), result)
    }

    @Test
    fun `div with a scalar should scale down the triplet`() {
        val t1 = Triplet(x = 10, y = 20, z = 30)

        val result = t1 / 10
        assertEquals(Triplet(1, 2, 3), result)
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

    //    fun lengthSquared() = dot(this)
    //    fun length() = sqrt(lengthSquared())
    //    fun normalize() = this / length()
    // nearZero
    @Test
    fun `lengthSquared should return the square of the euclidean distance`() {
        val t1 = Triplet(x = 2, y = 2, z = 3)

        assertEquals(17.0, t1.lengthSquared())
    }

    @Test
    fun `length should return the euclidean distance`() {
        val t1 = Triplet(x = 2, y = 3, z = 6)

        assertEquals(7.0, t1.length())
    }

    @Test
    fun `normalize should produce a length 1 vector`() {
        val v1 = Vector(x = 1, y = 2, z = 3)

        val result = v1.normalize()
        assertEquals(1.0, result.length(), EPSILON)
    }

    @Test
    fun `nearZero should return true for a triplet with very small magnitude components`() {
        val t1 = Triplet(x = EPSILON / 2, y = EPSILON / 2, z = EPSILON / 2)
        val t2 = Triplet(x = EPSILON / 2, y = EPSILON / 2, z = EPSILON * 2)

        assertTrue(t1.nearZero())
        assertFalse(t2.nearZero())
    }

    @Test
    fun testReflect() {
        val v1 = Vector(1.0, 2.0, 3.0)
        val n = Vector(0.0, 1.0, 0.0)

        val result = v1.reflect(n)
        assertEquals(Vector(1, -2, 3), result)
    }

    @Test
    fun testRefract() {
        val v1 = Vector(2.0, 2.0, 1 / 1.5)
        val n = Vector(0.0, 1.0, 0.0)

        // cosTheta = -v1.n = -2
        // Rperp = ([2,2,1/1.5] - 2*[0,-1,0]) * 1.5 = [3,0,1] (len = 10)
        // Rparallel = -sqrt(|1 - 10|) * [0,1,0] = [0,-3,0]
        val result: Triplet = v1.refract(n, 1.5)
        assertEquals(Vector(3, -3, 1), result)
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
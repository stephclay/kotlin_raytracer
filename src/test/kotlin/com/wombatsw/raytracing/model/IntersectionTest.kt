package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntersectionTest {
    val ray = Ray(Point(0, 0, 0), Vector(1, 0, 0))
    val p = Point(2, 0, 0)

    @Test
    fun `intersections from the outside of an object should have frontFace of true`() {
        val outerNormal = Vector(-1, 0, 0)

        val isect = Intersection(ray, 2.0, p, outerNormal, 1.0, 2.0)

        assertTrue(isect.frontFace)
        assertEquals(outerNormal, isect.n)
    }

    @Test
    fun `intersections from the inside of an object should have frontFace of false`() {
        val outerNormal = Vector(1, 0, 0)

        val isect = Intersection(ray, 2.0, p, outerNormal, 1.0, 2.0)

        assertFalse(isect.frontFace)
        assertEquals(-outerNormal, isect.n)
    }
}
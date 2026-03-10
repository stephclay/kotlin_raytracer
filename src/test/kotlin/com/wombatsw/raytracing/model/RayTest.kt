package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RayTest {
    @Test
    fun `should return a point along the ray`() {
        val origin = Point(1, 1, 1)
        val direction = Vector(0, 2, 3)
        val ray = Ray(origin, direction)

        val result = ray.at(4)

        assertEquals(Point(1, 9, 13), result)
    }
}
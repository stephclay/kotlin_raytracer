package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class CameraTest {
    @Test
    fun `focused camera should return a fixed point`() {
        val camera = createCamera(0)
        val result = camera.rayForPoint(Point(0, 0, 10))

        val expected = Ray(camera.cameraCenter, Vector(0, -1, 11))
        assertEquals(expected, result)
    }

    @Test
    fun `focused camera should return the same point on each call`() {
        val camera = createCamera(0)
        val r1 = camera.rayForPoint(Point(0, 0, 10))

        repeat(20) {
            val r2 = camera.rayForPoint(Point(0, 0, 10))
            assertEquals(r1, r2)
        }
    }

    @Test
    fun `unfocused camera should return different points on each call`() {
        val camera = createCamera(10)
        val r1 = camera.rayForPoint(Point(0, 0, 10))

        repeat(20) {
            val r2 = camera.rayForPoint(Point(0, 0, 10))
            if (r1 != r2) return
        }
        fail("Defocused camera always returning the same points")
    }

    fun createCamera(defocusAngle: Number) =
        Camera(
            100, 100,
            BLACK, Point(0, 1, -1),
            defocusAngle.toDouble(), 10.0, 90.0,
            Point(0, 0, 10),
            Vector(0, 1, 0)
        )
}
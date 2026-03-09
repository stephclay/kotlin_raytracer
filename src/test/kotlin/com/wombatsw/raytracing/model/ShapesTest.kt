package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ShapesTest {
    val material = Lambertian(SolidColor(Color(1, 0, 0)))

    @Test
    fun `sphere should have a bounding box whose sides match its diameter`() {
        val sphere = Sphere(Point(1, 1, 1), 2.0, material)

        assertEquals(4.0, sphere.boundingBox().x.size())
        assertEquals(4.0, sphere.boundingBox().y.size())
        assertEquals(4.0, sphere.boundingBox().z.size())
    }
}
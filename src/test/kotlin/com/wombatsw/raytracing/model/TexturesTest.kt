package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TexturesTest {
    @Test
    fun `SolidColor should return same value everywhere`() {
        val color = Color(1, 0, 0)
        val texture = SolidColor(color)

        val c1 = texture.value(Point(1, 1, 1), 1.0, v = 1.0)
        val c2 = texture.value(Point(5, -7, -8), 2.0, v = 3.0)

        assertEquals(color, c1)
        assertEquals(color, c2)
    }

    @Test
    fun `Checker texture should alternate color values`() {
        val t1 = SolidColor(Color(1, 0, 0))
        val t2 = SolidColor(Color(0, 0, 1))
        val texture = CheckerTexture(2.0, t1, t2)

        val c1 = texture.value(Point(1, 0, 0), 1.0, v = 1.0)
        val c2 = texture.value(Point(3, 0, 0), 1.0, v = 1.0)
        val c3 = texture.value(Point(5, 0, 0), 1.0, v = 1.0)
        val c4 = texture.value(Point(7, 0, 0), 1.0, v = 1.0)

        assertEquals(t1.color, c1)
        assertEquals(t2.color, c2)
        assertEquals(t1.color, c3)
        assertEquals(t2.color, c4)
    }

    @Test
    fun `Image texture should map data from image`() {
        // Test image is a 2x2 image with no compression, so color will be as expected
        val texture = ImageTexture("testimage.png")

        val c1 = texture.value(Point(1, 1, 1), 0.0, v = 0.0)
        val c2 = texture.value(Point(1, 1, 1), 1.0, v = 0.0)
        val c3 = texture.value(Point(1, 1, 1), 0.0, v = 1.0)
        val c4 = texture.value(Point(1, 1, 1), 1.0, v = 1.0)

        assertEquals(Color(0, 0, 1), c1)
        assertEquals(Color(0, 0, 0), c2)
        assertEquals(Color(1, 1, 1), c3)
        assertEquals(Color(0, 1, 0), c4)
    }

    @Test
    fun `Noise texture should produce different adjacent color values`() {
        // A better test would check that the value distribution matches expectations
        // but that is a lot more complicated to verify
        val texture = NoiseTexture(1.0)

        val c1 = texture.value(Point(1, 1, 1), 0.0, v = 0.0)
        val c2 = texture.value(Point(1.1, 1, 1), 0.0, v = 0.0)

        assertNotEquals(c1, c2)
    }
}
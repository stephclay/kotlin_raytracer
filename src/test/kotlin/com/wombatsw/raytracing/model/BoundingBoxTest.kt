package com.wombatsw.raytracing.model

import kotlin.test.*

class BoundingBoxTest {
    @Test
    fun `result of adding two bounding boxes should include both boxes`() {
        val b1 = BoundingBox(Interval(1.0, 2.0), Interval(1.1, 2.1), Interval(1.2, 2.3))
        val b2 = BoundingBox(Interval(3.0, 4.0), Interval(3.1, 4.1), Interval(3.2, 4.3))

        val result = b1 + b2
        for (i in 0..2) {
            val resultInterval = result.axisInterval(i)
            val b1Interval = b1.axisInterval(i)
            val b2Interval = b2.axisInterval(i)
            assertTrue(resultInterval.contains(b1Interval.min))
            assertTrue(resultInterval.contains(b1Interval.max))
            assertTrue(resultInterval.contains(b2Interval.min))
            assertTrue(resultInterval.contains(b2Interval.max))
        }
    }

    @Test
    fun `should be able to obtain interval by axis index`() {
        val bbox = BoundingBox(Interval(1.0, 2.0), Interval(3.0, 4.0), Interval(5.0, 6.0))

        assertEquals(Interval(1.0, 2.0), bbox.axisInterval(0))
        assertEquals(Interval(3.0, 4.0), bbox.axisInterval(1))
        assertEquals(Interval(5.0, 6.0), bbox.axisInterval(2))
    }

    @Test
    fun `should not accept invalid axis index`() {
        assertFailsWith<IllegalStateException>("Invalid axis: 3") {
            val bbox = BoundingBox(Interval(1.0, 2.0), Interval(3.0, 4.0), Interval(5.0, 6.0))
            bbox.axisInterval(3)
        }
    }

    @Test
    fun `should return longest axis index`() {
        val bbox = BoundingBox(Interval(1.0, 2.0), Interval(3.0, 4.0), Interval(1.0, 6.0))

        assertEquals(2, bbox.longestAxis())
    }

    @Test
    fun `should have minimum interval size`() {
        val bbox = BoundingBox(Point(0, 0, 0), Point(0, 0, 0))

        assertTrue(bbox.x.size() > 0.0)
        assertTrue(bbox.y.size() > 0.0)
        assertTrue(bbox.z.size() > 0.0)
    }

    @Test
    fun `should intersect from inside the box`() {
        val ray = Ray(Point(0, 0, 0), Point(0.5, 0, 0))
        val bbox = BoundingBox(Point(-1, -1, -1), Point(1, 1, 1))

        val result = bbox.intersect(ray, Interval(0, 100))
        assertNotNull(result)
        assertEquals(Interval(0.0, 2.0), result)
    }

    @Test
    fun `should intersect from outside the box`() {
        val ray = Ray(Point(-2, 0, 0), Point(0.5, 0, 0))
        val bbox = BoundingBox(Point(-1, -1, -1), Point(1, 1, 1))

        val result = bbox.intersect(ray, Interval(0, 100))
        assertNotNull(result)
        assertEquals(Interval(2.0, 6.0), result)
    }
}
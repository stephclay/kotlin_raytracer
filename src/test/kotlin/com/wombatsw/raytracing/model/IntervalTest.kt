package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntervalTest {
    @Test
    fun `size should reflect the length of the interval`() {
        assertEquals(3.0, Interval(1.0, 4.0).size())
    }

    @Test
    fun `overlapping intervals should add`() {
        val i1 = Interval(1.0, 4.0)
        val i2 = Interval(2.0, 5.0)

        val result = i1 + i2
        assertEquals(result.min, 1.0)
        assertEquals(result.max, 5.0)
    }

    @Test
    fun `non-overlapping intervals should add`() {
        val i1 = Interval(2.0, 3.0)
        val i2 = Interval(5.0, 6.0)

        val result = i1 + i2
        assertEquals(result.min, 2.0)
        assertEquals(result.max, 6.0)
    }

    @Test
    fun `contains method should include endpoints`() {
        val i1 = Interval(1.0, 4.0)

        assertTrue(i1.contains(1.0))
        assertTrue(i1.contains(4.0))
    }

    @Test
    fun `surrounds method should not include endpoints`() {
        val i1 = Interval(1.0, 4.0)

        assertFalse(i1.surrounds(1.0))
        assertFalse(i1.surrounds(4.0))
        assertTrue(i1.surrounds(1.00000001))
    }

    @Test
    fun `clamp should limit input value to interval`() {
        val i1 = Interval(1.0, 4.0)

        assertEquals(1.0, i1.clamp(0.0))
        assertEquals(2.0, i1.clamp(2.0))
        assertEquals(4.0, i1.clamp(5.0))
    }

    @Test
    fun `expand should increase the size of the interval`() {
        val i1 = Interval(1.0, 4.0)
        val result = i1.expand(2.0)

        assertEquals(0.0, result.min)
        assertEquals(5.0, result.max)
    }

    @Test
    fun `createOrdered should order the arguments as min less than max`() {
        val i1 = Interval.createOrdered(4.0, 1.0)

        assertEquals(1.0, i1.min)
        assertEquals(4.0, i1.max)
    }
}
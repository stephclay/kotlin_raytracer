package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals

class TripletTest {
    @Test
    fun `plus should do component level addition`() {
        val t1 = Triplet(x = 1.0, y = 2.0, z = 3.0)
        val t2 = Triplet(x = 4.0, y = 5.0, z = 6.0)

        val result = t1 + t2
        assertEquals(Triplet(5.0, 7.0, 9.0), result)
    }

    @Test
    fun `minus should do component level subtraction`() {
        val t1 = Triplet(x = 1.0, y = 2.0, z = 3.0)
        val t2 = Triplet(x = 4.0, y = 5.0, z = 6.0)

        val result = t1 - t2
        assertEquals(Triplet(-3.0, -3.0, -3.0), result)
    }
}
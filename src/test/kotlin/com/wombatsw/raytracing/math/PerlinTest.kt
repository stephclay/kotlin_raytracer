package com.wombatsw.raytracing.math

import com.wombatsw.raytracing.model.Triplet
import kotlin.test.Test
import kotlin.test.assertContains

// These tests are rather basic, since it is non-trivial to test the noise behavior
class PerlinTest {
    val perlin = Perlin()

    @Test
    fun `noise should stay within bounds`() {
        repeat(100) {
            val result = perlin.noise(Triplet.random(-20, 30))
            assertContains(-1.0..1.0, result)
        }
    }

    @Test
    fun `turbulence should stay within bounds`() {
        repeat(100) {
            val result = perlin.turbulence(Triplet.random(-20, 30), 7)
            assertContains(-1.0..1.0, result)
        }
    }
}

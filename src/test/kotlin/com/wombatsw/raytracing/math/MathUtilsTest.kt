package com.wombatsw.raytracing.math

import com.wombatsw.raytracing.math.MathUtils.randomDouble
import com.wombatsw.raytracing.math.MathUtils.randomInUnitDisc
import com.wombatsw.raytracing.math.MathUtils.randomInt
import kotlin.test.Test
import kotlin.test.assertContains

class MathUtilsTest {
    @Test
    fun `random ints should stay within specified bounds`() {
        repeat(100) {
            val result = randomInt(2, 200)
            assertContains(2..<200, result)
        }
    }

    @Test
    fun `random doubles should stay within default bounds`() {
        repeat(100) {
            val result = randomDouble()
            assertContains(0.0..<1.0, result)
        }
    }

    @Test
    fun `random doubles should stay within specified bounds`() {
        repeat(100) {
            val result = randomDouble(2, 200)
            assertContains(2.0..<200.0, result)
        }
    }

    @Test
    fun `random within unit disc should stay within default bounds`() {
        repeat(100) {
            val result = randomInUnitDisc()
            val radius = result.x * result.x + result.y * result.y
            assertContains(0.0..<1.0, radius)
        }
    }
}

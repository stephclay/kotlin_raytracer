package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.EPSILON
import kotlin.test.*

class MaterialsTest {
    val intersection = Intersection(
        Ray(Point(0, 0, 0), Vector(0, 0, 1)),
        2.0,
        Point(0, 0, 2),
        Vector(0, 0, -1),
        1.0, 1.0
    )
    val lambertian = Lambertian(SolidColor(WHITE))
    val dielectric = Dielectric(0.7)
    val metal = Metal(WHITE, 0.0)
    val light = DiffuseLight(SolidColor(WHITE))

    @Test
    fun `Lambertian should reflect rays at a random angle`() {
        val s1 = lambertian.scatter(intersection)

        verifyRandom(100, "Lambertian reflections all point the same direction") {
            val s2 = lambertian.scatter(intersection)
            s1.ray.direction != s2.ray.direction
        }
    }

    @Test
    fun `Dielectric should refract non-glancing rays at a fixed angle`() {
        verifyRandom(100, "Dielectric refracted at the wrong angle") {
            val scatterData = dielectric.scatter(intersection)

            Vector(0, 0, 1) == scatterData.ray.direction
        }
    }

    @Test
    fun `Dielectric should reflect glancing rays`() {
        val glancingIntersection = Intersection(
            Ray(Point(0, 0, 0), Vector(0, 0, 1)),
            2.0,
            Point(0, 0, 2),
            Vector(0, -1, -0.01),
            1.0, 1.0
        )

        val scatterData = dielectric.scatter(glancingIntersection)

        assertTrue(scatterData.ray.direction.y < 0.0)
    }

    @Test
    fun `Metal should reflect the ray at a fixed angle`() {
        val isectAt45DegreeAngle = Intersection(
            Ray(Point(0, 0, 0), Vector(0, 0, 1)),
            2.0,
            Point(0, 0, 2),
            Vector(0, -1, -1).normalize(),
            1.0, 1.0
        )

        val scatterData = metal.scatter(isectAt45DegreeAngle)
        val rayDir = scatterData!!.ray.direction
        // Need to compare each component due to rounding issues
        assertEquals(0.0, rayDir.x, EPSILON)
        assertEquals(-1.0, rayDir.y, EPSILON)
        assertEquals(0.0, rayDir.z, EPSILON)
    }

    @Test
    fun `DiffuseLight should not return any scatter data`() {
        assertNull(light.scatter(intersection))
    }

    @Test
    fun `emitted method should return black for materials which do not emit light`() {
        assertEquals(BLACK, lambertian.emitted(intersection))
        assertEquals(BLACK, dielectric.emitted(intersection))
        assertEquals(BLACK, metal.emitted(intersection))
    }

    @Test
    fun `emitted method should return non-black for materials which emit light`() {
        assertEquals(WHITE, light.emitted(intersection))
    }

    /**
     * Verify that the given block returns true at least once in the given number of iterations
     *
     * @param[iterations] The number of times to attempt the verification
     * @param[failMessage] The message to use if the verification fails for all iterations
     * @param[block] The block to execute, which returns true on success
     */
    fun verifyRandom(iterations: Int, failMessage: String, block: () -> Boolean) {
        repeat(iterations) {
            val result = block()
            if (result) return
        }
        fail(failMessage)
    }
}
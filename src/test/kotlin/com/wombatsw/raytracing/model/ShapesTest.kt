package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ShapesTest {
    val material = Lambertian(SolidColor(Color(1, 0, 0)))

    @Test
    fun `a sphere should have a bounding box whose sides match its diameter`() {
        val sphere = Sphere(Point(1, 1, 1), 2.0, material)

        assertEquals(4.0, sphere.boundingBox().x.size())
        assertEquals(4.0, sphere.boundingBox().y.size())
        assertEquals(4.0, sphere.boundingBox().z.size())
    }

    @Test
    fun `a sphere should return an intersection from outside`() {
        val sphere = Sphere(Point(1, 1, 1), 2.0, material)
        val ray = Ray(Point(-9, 1, 1), Vector(4, 0, 0))
        val tRange = Interval(0, Double.POSITIVE_INFINITY)

        val result = sphere.intersect(ray, tRange)

        assertNotNull(result)
        val intersection = result.first
        assertEquals(Point(-1, 1, 1), intersection.p)
        assertEquals(Vector(-1, 0, 0), intersection.n)
        assertEquals(2.0, intersection.t)
        assertEquals(0.0, intersection.u)
        assertEquals(0.5, intersection.v)
    }

    @Test
    fun `a sphere should return an intersection from inside`() {
        val sphere = Sphere(Point(1, 1, 1), 2.0, material)
        val ray = Ray(Point(1, 1, 1), Vector(4, 0, 0))
        val tRange = Interval(0, Double.POSITIVE_INFINITY)

        val result = sphere.intersect(ray, tRange)

        assertNotNull(result)
        val intersection = result.first
        assertEquals(Point(3, 1, 1), intersection.p)
        assertEquals(Vector(-1, 0, 0), intersection.n)
        assertEquals(0.5, intersection.t)
        assertEquals(0.5, intersection.u)
        assertEquals(0.5, intersection.v)
    }

    @Test
    fun `a sphere should not return an intersection when the ray misses`() {
        val sphere = Sphere(Point(1, 1, 1), 2.0, material)
        val ray = Ray(Point(-9, 1, 1), Vector(0, 4, 0))
        val tRange = Interval(0, Double.POSITIVE_INFINITY)

        val result = sphere.intersect(ray, tRange)

        assertNull(result)
    }

    @Test
    fun `a sphere should not return an intersection outside of the tRange`() {
        val sphere = Sphere(Point(1, 1, 1), 2.0, material)
        val ray = Ray(Point(-9, 1, 1), Vector(4, 0, 0))
        val tRange = Interval(0, 1)

        val result = sphere.intersect(ray, tRange)

        assertNull(result)
    }

    @Test
    fun `a quad should have a bounding box whose sides match its extremities`() {
        val quad = Quad(
            Point(1, 1, 1),
            Vector(1, 0, 0),
            Vector(0, 1, 1), material
        )

        assertEquals(1.0, quad.boundingBox().x.min)
        assertEquals(1.0, quad.boundingBox().y.min)
        assertEquals(1.0, quad.boundingBox().z.min)
        assertEquals(2.0, quad.boundingBox().x.max)
        assertEquals(2.0, quad.boundingBox().y.max)
        assertEquals(2.0, quad.boundingBox().z.max)
    }

    @Test
    fun `a quad should return an intersection`() {
        val quad = Quad(
            Point(1, 1, 1),
            Vector(2, 0, 0),
            Vector(0, 2, 0), material
        )
        val ray = Ray(Point(2, 2, -2), Vector(0, 0, 1))
        val tRange = Interval(0, Double.POSITIVE_INFINITY)

        val result = quad.intersect(ray, tRange)

        assertNotNull(result)
        val intersection = result.first
        assertEquals(Point(2, 2, 1), intersection.p)
        assertEquals(Vector(0, 0, -1), intersection.n)
        assertEquals(3.0, intersection.t)
        assertEquals(0.5, intersection.u)
        assertEquals(0.5, intersection.v)
    }

    @Test
    fun `a quat should not return an intersection when the ray misses`() {
        val quad = Quad(
            Point(1, 1, 1),
            Vector(2, 0, 0),
            Vector(0, 2, 0), material
        )
        val ray = Ray(Point(2, 2, -2), Vector(0, 1, 0))
        val tRange = Interval(0, Double.POSITIVE_INFINITY)

        val result = quad.intersect(ray, tRange)
        assertNull(result)
    }

    @Test
    fun `a quad should not return an intersection outside of the tRange`() {
        val quad = Quad(
            Point(1, 1, 1),
            Vector(2, 0, 0),
            Vector(0, 2, 0), material
        )
        val ray = Ray(Point(2, 2, -2), Vector(0, 0, 1))
        val tRange = Interval(0, 1)

        val result = quad.intersect(ray, tRange)

        assertNull(result)
    }


}
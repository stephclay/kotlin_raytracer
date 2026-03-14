package com.wombatsw.raytracing.model

import kotlin.test.*

class BVHNodeTest {
    val material = Lambertian(SolidColor(Color(1, 0, 0)))
    val material2 = Lambertian(SolidColor(Color(0, 0, 1)))
    val node1 = Sphere(Point(0, 0, 0), 1.0, material)
    val node2 = Sphere(Point(1, 0, 0), 3.0, material)
    val node3 = Sphere(Point(2, 0, 0), 2.0, material)
    val node4 = Sphere(Point(3, 0, 0), 1.0, material2)
    val initialTRange = Interval(0, Double.POSITIVE_INFINITY)

    @Test
    fun `left and right should have the same value for a single node list`() {
        val bvhNode = BVHNode(listOf(node1))

        assertSame(bvhNode.left, bvhNode.right)
    }

    @Test
    fun `BVH node should have a depth of one for a two node list`() {
        val bvhNode = BVHNode(listOf(node1, node2))

        assertIs<Sphere>(bvhNode.left)
        assertIs<Sphere>(bvhNode.right)
    }

    @Test
    fun `BVH node should have a bounding box encompassing the entire list`() {
        val bvhNode = BVHNode(listOf(node1, node2, node3))

        val bbox = bvhNode.boundingBox()

        assertEquals(Interval(-2, 4), bbox.x)
        assertEquals(Interval(-3, 3), bbox.y)
        assertEquals(Interval(-3, 3), bbox.z)
    }

    @Test
    fun `should intersect with left node`() {
        val bvhNode = BVHNode(listOf(node1, node4))
        val ray = Ray(Point(0, -10, 0), Vector(0, 1, 0))

        assertNotNull(node1.intersect(ray, initialTRange))
        val result = bvhNode.intersect(ray, initialTRange)

        assertNotNull(result)
        assertEquals(material, result.second)
        assertEquals(0.0, result.first.p.x)
    }

    @Test
    fun `should intersect with right node`() {
        val bvhNode = BVHNode(listOf(node1, node4))
        val ray = Ray(Point(3, -10, 0), Vector(0, 1, 0))

        val result = bvhNode.intersect(ray, initialTRange)

        assertNotNull(result)
        assertEquals(material2, result.second)
        assertEquals(3.0, result.first.p.x)
    }

    @Test
    fun `should miss when does not hit bounding box`() {
        val bvhNode = BVHNode(listOf(node1, node4))
        val ray = Ray(Point(0, -10, 0), Vector(0, -1, 0))

        val result = bvhNode.intersect(ray, initialTRange)

        assertNull(result)
    }

    @Test
    fun `should miss when it hits bounding box but not a contained object`() {
        val bvhNode = BVHNode(listOf(node1, node4))
        val ray = Ray(Point(1.5, -10, 0), Vector(0, 1, 0))

        val result = bvhNode.intersect(ray, initialTRange)

        assertNull(result)
    }
}
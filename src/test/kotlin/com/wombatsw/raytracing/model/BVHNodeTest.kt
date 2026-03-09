package com.wombatsw.raytracing.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class BVHNodeTest {
    val material = Lambertian(SolidColor(Color(1, 0, 0)))
    val node1 = Sphere(Point(0, 0, 0), 1.0, material)
    val node2 = Sphere(Point(1, 0, 0), 3.0, material)
    val node3 = Sphere(Point(2, 0, 0), 2.0, material)

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

}
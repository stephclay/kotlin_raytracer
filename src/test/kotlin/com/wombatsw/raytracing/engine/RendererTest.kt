package com.wombatsw.raytracing.engine

import com.wombatsw.raytracing.model.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RendererTest {
    @Test
    fun `render should return the background color after not hitting an object`() {
        val scene = createScene()
        val result = renderScene(scene)

        assertEquals(0.toByte(), result[0])
        assertEquals(0.toByte(), result[1])
        assertEquals(0.toByte(), result[2])
    }

    @Test
    fun `render should return a non-background color after hitting an object`() {
        val scene = createScene()
        val result = renderScene(scene)

        val w = scene.camera.imageWidth
        val h = scene.camera.imageHeight
        val centerIndex = (h * w / 2 + w / 2) * 3
        assertEquals(255.toByte(), result[centerIndex])
        assertEquals(255.toByte(), result[centerIndex + 1])
        assertEquals(255.toByte(), result[centerIndex + 2])
    }

    fun renderScene(scene: Scene): ByteArray {
        val renderer = Renderer(scene, 1, 1)

        return renderer.render()
    }

    fun createScene(): Scene {
        val material = DiffuseLight(SolidColor(WHITE))
        val sphere = Sphere(Point(0, 0, 10), 1.0, material)

        val camera = Camera(
            100, 100,
            BLACK, Point(0, 0, -10),
            0.0, 10.0, 90.0,
            Point(0, 0, 0),
            Vector(0, 1, 0)
        )

        return Scene(camera, listOf(sphere))
    }
}
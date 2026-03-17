package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Color
import com.wombatsw.raytracing.model.Vector
import com.wombatsw.raytracing.scene.InlineRef
import com.wombatsw.raytracing.scene.ResolutionContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CameraDTOTest {
    @Test
    fun `should resolve to Camera with default values`() {
        val ctx = ResolutionContext(SceneDTO())
        val dto = CameraDTO()

        val camera = dto.resolve(ctx)
        assertNotNull(camera)
        assertEquals(100, camera.imageWidth)
        assertEquals(100, camera.imageHeight)
        assertEquals(Color(0.0, 0.0, 0.0), camera.background)
        assertEquals(Vector(0.0, 1.0, 0.0), camera.viewUp)
    }

    @Test
    fun `should resolve to Camera with custom values`() {
        val ctx = ResolutionContext(SceneDTO())
        val white = TripletDTO(1.0, 1.0, 1.0)
        val dto = CameraDTO(200, 200, InlineRef(white))

        val camera = dto.resolve(ctx)
        assertNotNull(camera)
        assertEquals(200, camera.imageWidth)
        assertEquals(200, camera.imageHeight)
        assertEquals(Color(1.0, 1.0, 1.0), camera.background)
        assertEquals(Vector(0.0, 1.0, 0.0), camera.viewUp)
    }
}
package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Point
import com.wombatsw.raytracing.model.Sphere
import com.wombatsw.raytracing.scene.InlineRef
import com.wombatsw.raytracing.scene.ResolutionContext
import kotlin.test.Test
import kotlin.test.assertEquals

class SceneDTOTest {
    @Test
    fun `should resolve to Scene`() {
        val dto = createSceneDTO()
        val ctx = ResolutionContext(dto)

        val scene = dto.resolve(ctx)

        assertEquals(100, scene.camera.imageWidth)

        assertEquals(1, scene.world.size)
        val sphere = scene.world[0] as Sphere
        assertEquals(Point(2.0, 2.0, 2.0), sphere.center)
        assertEquals(3.0, sphere.radius)
    }

    fun createSceneDTO(): SceneDTO {
        val centerRef = InlineRef(TripletDTO(listOf(2.0, 2.0, 2.0)))
        val redColorRef = InlineRef(TripletDTO(listOf(1.0, 0.0, 0.0)))
        val materialRef = InlineRef(LambertianDTO(redColorRef, null))
        val sphereRef = InlineRef(SphereDTO(centerRef, 3.0, materialRef))
        return SceneDTO(world = listOf(sphereRef))
    }
}
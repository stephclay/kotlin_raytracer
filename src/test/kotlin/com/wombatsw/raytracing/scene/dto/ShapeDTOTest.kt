package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Color
import com.wombatsw.raytracing.model.Lambertian
import com.wombatsw.raytracing.model.Point
import com.wombatsw.raytracing.model.SolidColor
import com.wombatsw.raytracing.scene.InlineRef
import com.wombatsw.raytracing.scene.ResolutionContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ShapeDTOTest {
    @Test
    fun `should resolve to Sphere`() {
        val ctx = ResolutionContext(SceneDTO())
        val dto = createSphereDTO()

        val sphere = dto.resolve(ctx)

        assertEquals(Point(2.0, 2.0, 2.0), sphere.center)
        assertEquals(3.0, sphere.radius)

        val mat = sphere.material as Lambertian
        val tex = mat.texture as SolidColor
        assertEquals(Color(1.0, 0.0, 0.0), tex.color)
    }

    fun createSphereDTO(): SphereDTO {
        val centerRef = InlineRef(TripletDTO(listOf(2.0, 2.0, 2.0)))
        val redColorRef = InlineRef(TripletDTO(listOf(1.0, 0.0, 0.0)))
        val materialRef = InlineRef(LambertianDTO(redColorRef, null))
        return SphereDTO(centerRef, 3.0, materialRef)
    }
}
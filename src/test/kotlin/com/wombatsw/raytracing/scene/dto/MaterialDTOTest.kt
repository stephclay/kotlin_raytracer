package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.*
import com.wombatsw.raytracing.scene.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MaterialDTOTest {
    val colorDTOs = listOf(
        TripletDTO(listOf(1.0, 0.0, 0.0)),
        TripletDTO(listOf(0.0, 1.0, 0.0)),
        TripletDTO(listOf(0.0, 0.0, 1.0)),
        TripletDTO(listOf(1.0, 1.0, 0.0)),
    )
    val colors = colorDTOs.map {
        Color(it.list[0], it.list[1], it.list[2])
    }

    @Test
    fun `LambertianDTO with named Texture Ref should resolve to a Lambertian material`() {
        val ctx = createContext()

        val dto = LambertianDTO(null, NamedRef("t1"))
        val result = dto.resolve(ctx)

        val expected = Lambertian(SolidColor(colors[1]))
        assertEquals(expected, result)
    }

    @Test
    fun `LambertianDTO with inline Texture Ref should resolve to a Lambertian material`() {
        val ctx = createContext()

        val solidDTORef = InlineRef(SolidColorDTO(InlineRef(colorDTOs[2])))
        val dto = LambertianDTO(null, solidDTORef)
        val result = dto.resolve(ctx)

        val expected = Lambertian(SolidColor(colors[2]))
        assertEquals(expected, result)
    }

    @Test
    fun `LambertianDTO with named Color Ref should resolve to a Lambertian material`() {
        val ctx = createContext()

        val dto = LambertianDTO(NamedRef("c1"), null)
        val result = dto.resolve(ctx)

        val expected = Lambertian(SolidColor(colors[0]))
        assertEquals(expected, result)
    }

    @Test
    fun `LambertianDTO with inline Color Ref should resolve to a Lambertian material`() {
        val ctx = createContext()

        val colorRef1 = InlineRef(colorDTOs[3])
        val dto = LambertianDTO(colorRef1, null)
        val result = dto.resolve(ctx)

        val expected = Lambertian(SolidColor(colors[3]))
        assertEquals(expected, result)
    }

    @Test
    fun `LambertianDTO with missing Refs should throw an error`() {
        val ctx = createContext()

        val dto = LambertianDTO(null, null)
        assertFailsWith<IllegalArgumentException>("Either albedo or texture is required") {
            dto.resolve(ctx)
        }
    }

    @Test
    fun `DielectricDTO should resolve to a Dielectric material`() {
        val ctx = createContext()

        val dto = DielectricDTO(1.5)
        val result = dto.resolve(ctx)

        assertEquals(Dielectric(1.5), result)
    }

    @Test
    fun `MetalDTO with a named albedo Ref should resolve to a Metal material`() {
        val ctx = createContext()

        val dto = MetalDTO(NamedRef("c1"), 1.5)
        val result = dto.resolve(ctx)

        assertEquals(Metal(colors[0], 1.5), result)
    }

    @Test
    fun `MetalDTO with an inline albedo Ref should resolve to a Metal material`() {
        val ctx = createContext()

        val dto = MetalDTO(InlineRef(colorDTOs[1]), 2.5)
        val result = dto.resolve(ctx)

        assertEquals(Metal(colors[1], 2.5), result)
    }

    @Test
    fun `DiffuseLightDTO with a named texture Ref should resolve to a DiffuseLight material`() {
        val ctx = createContext()

        val dto = DiffuseLightDTO(NamedRef("t1"))
        val result = dto.resolve(ctx)

        assertEquals(DiffuseLight(SolidColor(colors[1])), result)
    }

    @Test
    fun `DiffuseLightDTO with an inline texture Ref should resolve to a DiffuseLight material`() {
        val ctx = createContext()

        val dto = DiffuseLightDTO(InlineRef(SolidColorDTO(InlineRef(colorDTOs[1]))))
        val result = dto.resolve(ctx)

        assertEquals(DiffuseLight(SolidColor(colors[1])), result)
    }

    fun createContext(): ResolutionContext {
        val sceneDTO = SceneDTO(
            CameraDTO(),
            mapOf("c1" to colorDTOs[0]),
            mapOf(),
            mapOf(),
            mapOf(
                "t1" to SolidColorDTO(InlineRef(colorDTOs[1]))
            )
        )
        return ResolutionContext(sceneDTO)
    }
}
package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.*
import com.wombatsw.raytracing.scene.InlineRef
import com.wombatsw.raytracing.scene.NamedRef
import com.wombatsw.raytracing.scene.ResolutionContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class TextureDTOTest {
    val colorDTOs = listOf(
        TripletDTO(1.0, 0.0, 0.0),
        TripletDTO(0.0, 1.0, 0.0),
        TripletDTO(0.0, 0.0, 1.0),
        TripletDTO(1.0, 1.0, 0.0),
        TripletDTO(0.0, 1.0, 1.0),
        TripletDTO(1.0, 0.0, 1.0),
    )
    val colors = colorDTOs.map {
        Color(it.list[0], it.list[1], it.list[2])
    }

    @Test
    fun `SolidColorDTO with named Ref should resolve to a SolidColor`() {
        val ctx = createContext()

        val dto = SolidColorDTO(NamedRef("c1"))
        val result = dto.resolve(ctx)

        assertEquals(SolidColor(colors[0]), result)
    }

    @Test
    fun `SolidColorDTO with inline Ref should resolve to a SolidColor`() {
        val ctx = createContext()

        val dto = SolidColorDTO(InlineRef(colorDTOs[1]))
        val result = dto.resolve(ctx)

        assertEquals(SolidColor(colors[1]), result)
    }

    @Test
    fun `CheckerTextureDTO with named Texture Refs should resolve to a CheckerTexture`() {
        val ctx = createContext()

        val dto = CheckerTextureDTO(1.0, NamedRef("t1"), NamedRef("t2"), null, null)
        val result = dto.resolve(ctx)

        val expected = CheckerTexture(1.0, SolidColor(colors[2]), SolidColor(colors[3]))
        assertEquals(expected, result)
    }

    @Test
    fun `CheckerTextureDTO with inline Texture Refs should resolve to a CheckerTexture`() {
        val ctx = createContext()

        val solidDTORef1 = InlineRef(SolidColorDTO(InlineRef(colorDTOs[4])))
        val solidDTORef2 = InlineRef(SolidColorDTO(InlineRef(colorDTOs[5])))
        val dto = CheckerTextureDTO(2.0, solidDTORef1, solidDTORef2, null, null)
        val result = dto.resolve(ctx)

        val expected = CheckerTexture(2.0, SolidColor(colors[4]), SolidColor(colors[5]))
        assertEquals(expected, result)
    }

    @Test
    fun `CheckerTextureDTO with named Color Refs should resolve to a CheckerTexture`() {
        val ctx = createContext()

        val dto = CheckerTextureDTO(1.0, null, null, NamedRef("c1"), NamedRef("c2"))
        val result = dto.resolve(ctx)

        val expected = CheckerTexture(1.0, SolidColor(colors[0]), SolidColor(colors[1]))
        assertEquals(expected, result)
    }

    @Test
    fun `CheckerTextureDTO with inline Color Refs should resolve to a CheckerTexture`() {
        val ctx = createContext()

        val colorRef1 = InlineRef(colorDTOs[5])
        val colorRef2 = InlineRef(colorDTOs[4])
        val solidDTORef2 = InlineRef(SolidColorDTO(InlineRef(colorDTOs[5])))
        val dto = CheckerTextureDTO(3.0, null, null, colorRef1, colorRef2)
        val result = dto.resolve(ctx)

        val expected = CheckerTexture(3.0, SolidColor(colors[5]), SolidColor(colors[4]))
        assertEquals(expected, result)
    }

    @Test
    fun `CheckerTextureDTO with mixed Texture and Color Refs should resolve to a CheckerTexture`() {
        val ctx = createContext()

        val colorRef = InlineRef(colorDTOs[4])
        val dto = CheckerTextureDTO(5.0, NamedRef("t1"), null, null, colorRef)
        val result = dto.resolve(ctx)

        val expected = CheckerTexture(5.0, SolidColor(colors[2]), SolidColor(colors[4]))
        assertEquals(expected, result)
    }

    @Test
    fun `CheckerTextureDTO with missing even Refs should throw an error`() {
        val ctx = createContext()

        val dto = CheckerTextureDTO(5.0, null, NamedRef("t1"), null, null)
        assertFailsWith<IllegalArgumentException>("Either even or evenColor is required") {
            dto.resolve(ctx)
        }
    }

    @Test
    fun `CheckerTextureDTO with missing odd Refs should throw an error`() {
        val ctx = createContext()

        val dto = CheckerTextureDTO(5.0, NamedRef("t1"), null, null, null)
        assertFailsWith<IllegalArgumentException>("Either odd or oddColor is required") {
            dto.resolve(ctx)
        }
    }

    @Test
    fun `ImageTextureDTO should resolve to an ImageTexture`() {
        val ctx = createContext()

        val dto = ImageTextureDTO("testimage.png")
        val result = dto.resolve(ctx)

        assertIs<ImageTexture>(result)
    }

    @Test
    fun `NoiseTextureDTO should resolve to an NoiseTexture`() {
        val ctx = createContext()

        val dto = NoiseTextureDTO(1.5)
        val result = dto.resolve(ctx)

        assertEquals(NoiseTexture(1.5), result)
    }

    fun createContext(): ResolutionContext {
        val sceneDTO = SceneDTO(
            CameraDTO(),
            mapOf("c1" to colorDTOs[0], "c2" to colorDTOs[1]),
            mapOf(),
            mapOf(),
            mapOf(
                "t1" to SolidColorDTO(InlineRef(colorDTOs[2])),
                "t2" to SolidColorDTO(InlineRef(colorDTOs[3]))
            )
        )
        return ResolutionContext(sceneDTO)
    }
}
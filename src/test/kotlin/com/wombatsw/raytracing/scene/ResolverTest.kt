package com.wombatsw.raytracing.scene

import com.wombatsw.raytracing.model.Color
import com.wombatsw.raytracing.model.Lambertian
import com.wombatsw.raytracing.model.SolidColor
import com.wombatsw.raytracing.model.Triplet
import com.wombatsw.raytracing.scene.dto.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ResolverTest {
    val tripletDTOX = TripletDTO(listOf(1.0, 0.0, 0.0))
    val tripletDTOY = TripletDTO(listOf(0.0, 1.0, 0.0))
    val tripletDTOZ = TripletDTO(listOf(0.0, 0.0, 1.0))
    val tripletX = tripletFromDTO(tripletDTOX)
    val tripletY = tripletFromDTO(tripletDTOY)
    val tripletZ = tripletFromDTO(tripletDTOZ)

    private fun tripletFromDTO(tripletDTO: TripletDTO): Triplet =
        Triplet(tripletDTO.list[0], tripletDTO.list[1], tripletDTO.list[2])

    @Test
    fun `Color should resolve from named Ref`() {
        val ctx = createContext()

        val color = ctx.resolveColor(NamedRef("c1"))
        assertEquals(tripletX, color)
    }

    @Test
    fun `Color should resolve from inline Ref`() {
        val ctx = createContext()

        val color = ctx.resolveColor(InlineRef(TripletDTO(listOf(0.0, 1.0, 0.0))))
        assertEquals(tripletY, color)
    }

    @Test
    fun `Color should resolve from a named default color`() {
        val ctx = createContext()

        val color = ctx.resolveColor(NamedRef("cyan"))
        assertEquals(Color(0, 1, 1), color)
    }

    @Test
    fun `Color resolution should fail for nonexistent named Ref`() {
        assertFailsWith<IllegalArgumentException>("Expected exception for missing color") {
            val ctx = createContext()
            ctx.resolveColor(NamedRef("c2"))
        }
    }

    @Test
    fun `Vector should resolve from named Ref`() {
        val ctx = createContext()

        val vector = ctx.resolveVector(NamedRef("v1"))
        assertEquals(tripletY, vector)
    }

    @Test
    fun `Vector should resolve from inline Ref`() {
        val ctx = createContext()

        val vector = ctx.resolveVector(InlineRef(TripletDTO(listOf(0.0, 0.0, 1.0))))
        assertEquals(tripletZ, vector)
    }

    @Test
    fun `Vector resolution should fail for nonexistent named Ref`() {
        assertFailsWith<IllegalArgumentException>("Expected exception for missing vector") {
            val ctx = createContext()
            ctx.resolveVector(NamedRef("v2"))
        }
    }

    @Test
    fun `Point should resolve from named Ref`() {
        val ctx = createContext()

        val point = ctx.resolvePoint(NamedRef("p1"))
        assertEquals(tripletZ, point)
    }

    @Test
    fun `Point should resolve from inline Ref`() {
        val ctx = createContext()

        val point = ctx.resolvePoint(InlineRef(TripletDTO(listOf(1.0, 0.0, 0.0))))
        assertEquals(tripletX, point)
    }

    @Test
    fun `Point resolution should fail for nonexistent named Ref`() {
        assertFailsWith<IllegalArgumentException>("Expected exception for missing point") {
            val ctx = createContext()
            ctx.resolvePoint(NamedRef("p2"))
        }
    }

    @Test
    fun `Texture should resolve from named Ref`() {
        val ctx = createContext()

        val texture = ctx.resolveTexture(NamedRef("t1"))
        assertEquals(SolidColor(tripletX), texture)
    }

    @Test
    fun `Texture should resolve from inline Ref`() {
        val ctx = createContext()

        val texture = ctx.resolveTexture(InlineRef(SolidColorDTO(NamedRef("c1"))))
        assertEquals(SolidColor(tripletX), texture)
    }

    @Test
    fun `Texture resolution should fail for nonexistent named Ref`() {
        val ex = assertFailsWith<IllegalArgumentException>("Expected exception for missing texture") {
            val ctx = createContext()
            ctx.resolveTexture(NamedRef("t2"))
        }
    }

    @Test
    fun `Material should resolve from named Ref`() {
        val ctx = createContext()

        val material = ctx.resolveMaterial(NamedRef("m1"))
        assertEquals(Lambertian(SolidColor(tripletX)), material)
    }

    @Test
    fun `Material should resolve from inline Ref`() {
        val ctx = createContext()

        val material = ctx.resolveMaterial(InlineRef(LambertianDTO(null, NamedRef("t1"))))
        assertEquals(Lambertian(SolidColor(tripletX)), material)
    }

    @Test
    fun `Material resolution should fail for nonexistent named Ref`() {
        val ex = assertFailsWith<IllegalArgumentException>("Expected exception for missing material") {
            val ctx = createContext()
            ctx.resolveMaterial(NamedRef("t2"))
        }
    }

    fun createContext(): ResolutionContext {
        val sceneDTO = SceneDTO(
            CameraDTO(),
            mapOf("c1" to tripletDTOX),
            mapOf("v1" to tripletDTOY),
            mapOf("p1" to tripletDTOZ),
            mapOf("t1" to SolidColorDTO(NamedRef("c1"))),
            mapOf("m1" to LambertianDTO(null, NamedRef("t1")))
        )
        return ResolutionContext(sceneDTO)
    }
}
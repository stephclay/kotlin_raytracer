package com.wombatsw.raytracing.scene

import com.wombatsw.raytracing.model.Triplet
import com.wombatsw.raytracing.scene.dto.TripletDTO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class ResolverTest {
    @Test
    fun testResolveColorNamed() {
        val ctx = createContext()

        val color = ctx.resolveColor(NamedRef("c1"))
        validateTriplet(color, 1.0, 0.0, 0.0)
    }

    @Test
    fun testResolveColorInline() {
        val ctx = createContext()

        val color = ctx.resolveColor(InlineRef(TripletDTO(listOf(2.0, 0.0, 0.0))))
        validateTriplet(color, 2.0, 0.0, 0.0)
    }

    @Test
    fun testResolveColorNotFound() {
        val ex = assertFailsWith<IllegalArgumentException>("Expected exception for missing color") {
            val ctx = createContext()
            ctx.resolveColor(NamedRef("c2"))
        }
    }

    @Test
    fun testResolveVectorNamed() {
        val ctx = createContext()

        val vector = ctx.resolveVector(NamedRef("v1"))
        validateTriplet(vector, 0.0, 1.0, 0.0)
    }

    @Test
    fun testResolveVectorInline() {
        val ctx = createContext()

        val vector = ctx.resolveVector(InlineRef(TripletDTO(listOf(2.0, 0.0, 0.0))))
        validateTriplet(vector, 2.0, 0.0, 0.0)
    }

    @Test
    fun testResolveVectorNotFound() {
        val ex = assertFailsWith<IllegalArgumentException>("Expected exception for missing vector") {
            val ctx = createContext()
            ctx.resolveVector(NamedRef("v2"))
        }
    }

    @Test
    fun testResolvePointNamed() {
        val ctx = createContext()

        val point = ctx.resolvePoint(NamedRef("p1"))
        validateTriplet(point, 0.0, 0.0, 1.0)
    }

    @Test
    fun testResolvePointInline() {
        val ctx = createContext()

        val point = ctx.resolvePoint(InlineRef(TripletDTO(listOf(2.0, 0.0, 0.0))))
        validateTriplet(point, 2.0, 0.0, 0.0)
    }

    @Test
    fun testResolvePointNotFound() {
        val ex = assertFailsWith<IllegalArgumentException>("Expected exception for missing point") {
            val ctx = createContext()
            ctx.resolvePoint(NamedRef("p2"))
        }
    }

    fun validateTriplet(triplet: Triplet, x: Double, y: Double, z: Double) {
        assertNotNull(triplet)
        assertEquals(x, triplet.x)
        assertEquals(y, triplet.y)
        assertEquals(z, triplet.z)
    }

    fun createContext(): ResolutionContext {
        val sceneDTO = SceneDTO(
            CameraDTO(),
            mapOf("c1" to TripletDTO(listOf(1.0, 0.0, 0.0))),
            mapOf("v1" to TripletDTO(listOf(0.0, 1.0, 0.0))),
            mapOf("p1" to TripletDTO(listOf(0.0, 0.0, 1.0)))
        )
        return ResolutionContext(sceneDTO)
    }
}
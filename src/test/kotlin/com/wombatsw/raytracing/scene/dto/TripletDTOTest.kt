package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.scene.ResolutionContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TripletDTOTest {
    @Test
    fun `TripletDTO should resolve to a Triplet`() {
        val ctx = ResolutionContext(SceneDTO())
        val dto = TripletDTO(1.0, 2.0, 3.0)

        val triplet = dto.resolve(ctx)

        assertNotNull(triplet)
        assertEquals(1.0, triplet.x)
        assertEquals(2.0, triplet.y)
        assertEquals(3.0, triplet.z)
    }
}
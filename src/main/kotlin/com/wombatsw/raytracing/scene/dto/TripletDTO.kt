package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Triplet
import com.wombatsw.raytracing.scene.ResolutionContext
import com.wombatsw.raytracing.scene.Resolvable

data class TripletDTO(val list: List<Number>) : Resolvable<Triplet> {
    constructor(x: Number, y: Number, z: Number) : this(listOf(x.toDouble(), y.toDouble(), z.toDouble()))

    init {
        require(list.size == 3) { "Triplet requires an array of exactly 3 values" }
    }

    override fun resolve(ctx: ResolutionContext): Triplet =
        Triplet(list[0], list[1], list[2])
}
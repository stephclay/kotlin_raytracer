package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Triplet
import com.wombatsw.raytracing.scene.ResolutionContext
import com.wombatsw.raytracing.scene.Resolvable

data class TripletDTO(val x: Number, val y: Number, val z: Number) : Resolvable<Triplet> {
    constructor(list: List<Number>) : this(list[0], list[1], list[2])

    override fun resolve(ctx: ResolutionContext): Triplet =
        Triplet(x.toDouble(), y.toDouble(), z.toDouble())
}
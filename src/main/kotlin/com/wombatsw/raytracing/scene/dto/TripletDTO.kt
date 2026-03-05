package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Triplet
import com.wombatsw.raytracing.scene.ResolutionContext
import com.wombatsw.raytracing.scene.Resolvable

class TripletDTO(val list: List<Double>) : Resolvable<Triplet> {
    override fun resolve(ctx: ResolutionContext): Triplet =
        Triplet(list[0], list[1], list[2])
}
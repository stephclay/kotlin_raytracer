package com.wombatsw.raytracing.scene

interface Resolvable<T> {
    fun resolve(ctx: ResolutionContext): T
}
package com.wombatsw.raytracing.scene

import com.wombatsw.raytracing.model.Color
import com.wombatsw.raytracing.model.Point
import com.wombatsw.raytracing.model.Vector
import com.wombatsw.raytracing.scene.dto.TripletDTO


/**
 * Resolver for named entries in a scene file. Each resolved entry will be cached to avoid repeat work.
 *
 * @param[D] A [Resolvable] DTO
 * @param[T] The type that the DTO resolves to
 */
class Resolver<D : Resolvable<T>, T>(
    private val entries: Map<String, D>,
    private val kind: String
) {
    private val cache: MutableMap<Ref<D>, T> = mutableMapOf()

    /**
     * Resolve the provided [Ref]. Will look up a named reference or utilize an inline reference, then call that
     * object's resolution method.
     *
     * @param[ctx] The [ResolutionContext]
     * @param[ref] The reference to a DTO
     * @return The resolved object
     */
    fun resolve(ctx: ResolutionContext, ref: Ref<D>): T =
        cache.getOrPut(ref) {
            val dto = when (ref) {
                is InlineRef -> ref.value
                is NamedRef -> entries[ref.name]
                    ?: throw IllegalArgumentException("Unknown $kind ${ref.name}")
            }
            dto.resolve(ctx)
        }
}

/**
 * The resolution context, containing all named DTOs and methods to resolve them by type
 *
 * @param[sceneDTO] The scene data
 */
class ResolutionContext(sceneDTO: SceneDTO) {
    val colors: Resolver<TripletDTO, Color> = Resolver(sceneDTO.colors, "color")
    val vectors: Resolver<TripletDTO, Vector> = Resolver(sceneDTO.vectors, "vector")
    val points: Resolver<TripletDTO, Point> = Resolver(sceneDTO.points, "point")

    fun resolveColor(ref: Ref<TripletDTO>): Color = resolve(ref, colors)
    fun resolveVector(ref: Ref<TripletDTO>): Vector = resolve(ref, vectors)
    fun resolvePoint(ref: Ref<TripletDTO>): Point = resolve(ref, points)

    private fun <D : Resolvable<T>, T> resolve(ref: Ref<D>, resolver: Resolver<D, T>): T {
        return resolver.resolve(this, ref)
    }
}


package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.Scene
import com.wombatsw.raytracing.scene.Ref
import com.wombatsw.raytracing.scene.ResolutionContext
import com.wombatsw.raytracing.scene.Resolvable

/**
 * Scene DTO
 *
 * @property[camera] The Camera data
 * @property[colors] The color map
 * @property[vectors] The vector map
 * @property[points] The point map
 * @property[textures] The texture map
 * @property[materials] The material map
 * @property[objects] The object map
 * @property[world] The world object list
 */
class SceneDTO(
    val camera: CameraDTO = CameraDTO(),
    val colors: Map<String, TripletDTO> = mapOf(),
    val vectors: Map<String, TripletDTO> = mapOf(),
    val points: Map<String, TripletDTO> = mapOf(),
    val textures: Map<String, TextureDTO> = mapOf(),
    val materials: Map<String, MaterialDTO> = mapOf(),
    val objects: Map<String, ShapeDTO> = mapOf(),
    val world: List<Ref<ShapeDTO>> = listOf()
) : Resolvable<Scene> {
    override fun resolve(ctx: ResolutionContext): Scene {
        val objList = world.map { ctx.resolveObject(it) }
        return Scene(camera.resolve(ctx), objList)
    }

    override fun toString(): String {
        fun listToString(list: Collection<*>) =
            list.joinToString(separator = ",\n    ", prefix = "\n    ")

        fun mapToString(map: Map<String, *>) =
            listToString(map.map { (k, v) -> "$k - $v" })

        return "Config(\n" +
                "  camera=$camera,\n" +
                "  colors=${mapToString(colors)},\n" +
                "  vectors=${mapToString(vectors)},\n" +
                "  points=${mapToString(points)},\n" +
                "  textures=${mapToString(textures)},\n" +
                "  materials=${mapToString(materials)},\n" +
                "  objects=${mapToString(objects)},\n" +
                "  world=${listToString(world)})"
    }
}
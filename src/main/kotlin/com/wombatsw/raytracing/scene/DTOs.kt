package com.wombatsw.raytracing.scene

import com.wombatsw.raytracing.scene.dto.TripletDTO

// Scene file DTOs. All DTO parameters which can refer to other objects in the scene will use a reference
// instead of the actual class. Reference resolution is handled after the scene objects are loaded.

class SceneDTO(
    val camera: CameraDTO = CameraDTO(),
    val colors: Map<String, TripletDTO> = mapOf(),
    val vectors: Map<String, TripletDTO> = mapOf(),
    val points: Map<String, TripletDTO> = mapOf(),
    val textures: Map<String, TextureDTO> = mapOf(),
    val materials: Map<String, MaterialDTO> = mapOf(),
    val world: List<ShapeDTO> = listOf()
) {
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
                "  world=${listToString(world)})"
    }
}

data class CameraDTO(
    val imageWidth: Int = 100,
    val imageHeight: Int = 100,
    val background: Ref<TripletDTO> = InlineRef(TripletDTO(listOf(0.0, 0.0, 0.0))),
    val cameraCenter: Ref<TripletDTO> = InlineRef(TripletDTO(listOf(0.0, 0.0, 0.0))),
    val defocusAngle: Double = 0.0,
    val focusDistance: Double = 10.0,
    val fieldOfView: Double = 90.0,
    val viewportCenter: Ref<TripletDTO> = InlineRef(TripletDTO(listOf(0.0, 0.0, -1.0))),
    val viewUp: Ref<TripletDTO> = InlineRef(TripletDTO(listOf(0.0, 1.0, 0.0)))
)

sealed class TextureDTO: Resolvable<Any> {
    override fun resolve(ctx: ResolutionContext): Any {
        TODO("Not yet implemented")
    }
}
data class SolidColorDTO(val color: Ref<TripletDTO>) : TextureDTO()
data class CheckerTextureDTO(
    val scale: Double, val even: Ref<TextureDTO>?, val odd: Ref<TextureDTO>?,
    val evenColor: Ref<TripletDTO>?, val oddColor: Ref<TripletDTO>?
) : TextureDTO()

data class ImageTextureDTO(val filename: String) : TextureDTO()
data class NoiseTextureDTO(val scale: Double) : TextureDTO()

sealed class MaterialDTO: Resolvable<Any> {
    override fun resolve(ctx: ResolutionContext): Any {
        TODO("Not yet implemented")
    }
}
data class LambertianDTO(val albedo: Ref<TripletDTO>?, val texture: Ref<TextureDTO>?) : MaterialDTO()
data class DielectricDTO(val refractionIndex: Double) : MaterialDTO()
data class MetalDTO(val albedo: Ref<TripletDTO>, val fuzz: Double) : MaterialDTO()
data class DiffuseLightDTO(val texture: Ref<TextureDTO>) : MaterialDTO()

sealed class ShapeDTO(open val material: Ref<MaterialDTO>)
data class SphereDTO(val center: Ref<TripletDTO>, val radius: Double, override val material: Ref<MaterialDTO>) :
    ShapeDTO(material)

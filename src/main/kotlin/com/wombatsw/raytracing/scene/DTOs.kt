package com.wombatsw.raytracing.scene

import com.wombatsw.raytracing.scene.dto.MaterialDTO
import com.wombatsw.raytracing.scene.dto.TextureDTO
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

sealed class ShapeDTO(open val material: Ref<MaterialDTO>)
data class SphereDTO(val center: Ref<TripletDTO>, val radius: Double, override val material: Ref<MaterialDTO>) :
    ShapeDTO(material)

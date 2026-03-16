package com.wombatsw.raytracing.scene.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.wombatsw.raytracing.scene.dto.*

/**
 * TextureDTO deserializer
 */
class TextureDeserializer : PolymorphicDeserializer<TextureDTO>("texture") {
    override fun mapValue(p: JsonParser, type: String, value: JsonNode): TextureDTO {
        return when (type) {
            "SolidColor" -> p.codec.treeToValue(value, SolidColorDTO::class.java)
            "Checker" -> p.codec.treeToValue(value, CheckerTextureDTO::class.java)
            "Image" -> p.codec.treeToValue(value, ImageTextureDTO::class.java)
            "Noise" -> p.codec.treeToValue(value, NoiseTextureDTO::class.java)
            else -> error("Unknown texture type: $type")
        }
    }
}

/**
 * MaterialDTO deserializer
 */
class MaterialDeserializer : PolymorphicDeserializer<MaterialDTO>("material") {
    override fun mapValue(p: JsonParser, type: String, value: JsonNode): MaterialDTO {
        return when (type) {
            "Lambertian" -> p.codec.treeToValue(value, LambertianDTO::class.java)
            "Dielectric" -> p.codec.treeToValue(value, DielectricDTO::class.java)
            "Metal" -> p.codec.treeToValue(value, MetalDTO::class.java)
            "DiffuseLight" -> p.codec.treeToValue(value, DiffuseLightDTO::class.java)
            else -> error("Unknown material type: $type")
        }
    }
}

/**
 * ShapeDTO deserializer
 */
class ShapeDeserializer : PolymorphicDeserializer<ShapeDTO>("object") {
    override fun mapValue(p: JsonParser, type: String, value: JsonNode): ShapeDTO {
        return when (type) {
            "Sphere" -> p.codec.treeToValue(value, SphereDTO::class.java)
            "Quad" -> p.codec.treeToValue(value, QuadDTO::class.java)
            "Box" -> p.codec.treeToValue(value, BoxDTO::class.java)
            else -> error("Unknown object node type: $type")
        }
    }
}

/**
 * Common polymorphic DTL deserializer
 */
abstract class PolymorphicDeserializer<T>(val name: String) : JsonDeserializer<T>() {
    abstract fun mapValue(p: JsonParser, type: String, value: JsonNode): T

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        val node = p.codec.readTree<JsonNode>(p)
        require(node.size() == 1) { "Each $name must have exactly 1 type" }

        val (type, value) = node.properties().first()
        return mapValue(p, type, value)
    }
}


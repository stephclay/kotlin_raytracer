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
    override fun mapValue(ctxt: DeserializationContext, type: String, value: JsonNode): TextureDTO {
        return when (type) {
            "SolidColor" -> ctxt.readTreeAsValue(value, SolidColorDTO::class.java)
            "Checker" -> ctxt.readTreeAsValue(value, CheckerTextureDTO::class.java)
            "Image" -> ctxt.readTreeAsValue(value, ImageTextureDTO::class.java)
            "Noise" -> ctxt.readTreeAsValue(value, NoiseTextureDTO::class.java)
            else -> error("Unknown texture type: $type")
        }
    }
}

/**
 * MaterialDTO deserializer
 */
class MaterialDeserializer : PolymorphicDeserializer<MaterialDTO>("material") {
    override fun mapValue(ctxt: DeserializationContext, type: String, value: JsonNode): MaterialDTO {
        return when (type) {
            "Lambertian" -> ctxt.readTreeAsValue(value, LambertianDTO::class.java)
            "Dielectric" -> ctxt.readTreeAsValue(value, DielectricDTO::class.java)
            "Metal" -> ctxt.readTreeAsValue(value, MetalDTO::class.java)
            "DiffuseLight" -> ctxt.readTreeAsValue(value, DiffuseLightDTO::class.java)
            else -> error("Unknown material type: $type")
        }
    }
}

/**
 * ShapeDTO deserializer
 */
class ShapeDeserializer : PolymorphicDeserializer<ShapeDTO>("object") {
    override fun mapValue(ctxt: DeserializationContext, type: String, value: JsonNode): ShapeDTO {
        return when (type) {
            "Sphere" -> ctxt.readTreeAsValue(value, SphereDTO::class.java)
            "Quad" -> ctxt.readTreeAsValue(value, QuadDTO::class.java)
            "Box" -> ctxt.readTreeAsValue(value, BoxDTO::class.java)
            else -> error("Unknown object node type: $type")
        }
    }
}

/**
 * Common polymorphic DTL deserializer
 */
abstract class PolymorphicDeserializer<T>(val name: String) : JsonDeserializer<T>() {
    abstract fun mapValue(ctxt: DeserializationContext, type: String, value: JsonNode): T

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        val node = p.codec.readTree<JsonNode>(p)
        require(node.size() == 1) { "Each $name must have exactly 1 type" }

        val (type, value) = node.properties().first()
        return mapValue(ctxt, type, value)
    }
}


package com.wombatsw.raytracing.scene.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.wombatsw.raytracing.scene.InlineRef
import com.wombatsw.raytracing.scene.NamedRef
import com.wombatsw.raytracing.scene.Ref


/**
 * Reference deserializer
 */
class RefDeserializer : JsonDeserializer<Ref<*>>(), ContextualDeserializer {
    override fun createContextual(
        ctxt: DeserializationContext,
        property: BeanProperty?
    ): JsonDeserializer<Ref<*>> {

        val type = property?.type
            ?: ctxt.contextualType
            ?: error("Cannot determine Ref type")
        val valueType = type.containedType(0) ?: error("Ref must be parameterized")
        return TypedRefDeserializer(valueType)
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Ref<*> {
        error("RefDeserializer should not be called directly")
    }
}

/**
 * Type-specific reference deserializer. This class is used by RefDeserializer to convert a ref to either a named
 * or inline reference
 */
private class TypedRefDeserializer(val valueType: JavaType) : JsonDeserializer<Ref<*>>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Ref<*> {
        val node = p.codec.readTree<JsonNode>(p)
        return when {
            node.isTextual -> NamedRef(node.asText())
            node.isObject || node.isArray -> InlineRef<Any>(ctxt.readValue(node.traverse(p.codec), valueType))
            else -> error("Invalid reference: $valueType")
        }
    }
}

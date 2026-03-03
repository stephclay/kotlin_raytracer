package com.wombatsw.raytracing.scene.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.wombatsw.raytracing.scene.TripletDTO

/**
 * TripletDTO deserializer
 */
class TripletDeserializer : JsonDeserializer<TripletDTO>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): TripletDTO {
        val node = p.codec.readTree<JsonNode>(p)
        require(node.isArray && node.size() == 3) { "Triplet requires an array of 3 values" }
        return TripletDTO(node.map { it.asDouble() })
    }
}

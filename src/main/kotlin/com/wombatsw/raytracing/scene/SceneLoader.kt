package com.wombatsw.raytracing.scene

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.wombatsw.raytracing.scene.deserializer.*
import com.wombatsw.raytracing.scene.dto.*
import java.io.FileReader
import java.io.Reader

/**
 * Load a scene from a file or other reader
 */
class SceneLoader {
    private val mapper = ObjectMapper(YAMLFactory())
        .registerKotlinModule()
        .registerModule(
            SimpleModule().apply {
                addDeserializer(TextureDTO::class.java, TextureDeserializer())
                addDeserializer(MaterialDTO::class.java, MaterialDeserializer())
                addDeserializer(ShapeDTO::class.java, ShapeDeserializer())
                addDeserializer(Ref::class.java, RefDeserializer())
                addDeserializer(TripletDTO::class.java, TripletDeserializer())
            }
        )

    /**
     * Load a scene from the specified YAML file
     *
     * @param[fileName] The file containing the scene data
     * @return The [com.wombatsw.raytracing.scene.dto.SceneDTO]
     */
    fun loadScene(fileName: String): SceneDTO {
        return loadScene(FileReader(fileName))
    }

    /**
     * Load a scene from the specified Reader containing a YAML description
     *
     * @param[reader] The reader containing the scene data
     * @return The [SceneDTO]
     */
    fun loadScene(reader: Reader): SceneDTO {
        return mapper.readValue(reader, SceneDTO::class.java)
    }
}
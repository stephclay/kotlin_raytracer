package com.wombatsw.raytracing

import com.wombatsw.raytracing.engine.PPMImageWriter
import com.wombatsw.raytracing.engine.Renderer
import com.wombatsw.raytracing.scene.SceneLoader
import java.io.File


fun main() {
    val sceneFile = "src/main/resources/scenes/test.yaml"
    val loader = SceneLoader()
    val scene = loader.loadScene(sceneFile)

    val renderer = Renderer(scene, 40, 20)
    val writer = PPMImageWriter()

    val data = renderer.render()
    writer.write(File("test.ppm"), scene.camera, data)
}

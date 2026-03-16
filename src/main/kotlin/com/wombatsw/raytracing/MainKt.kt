package com.wombatsw.raytracing

import com.wombatsw.raytracing.engine.PPMImageWriter
import com.wombatsw.raytracing.engine.Renderer
import com.wombatsw.raytracing.scene.SceneLoader
import java.io.File


fun main() {
    val scenes = arrayOf(
        "src/main/resources/scenes/CheckerSpheres.yaml",
        "src/main/resources/scenes/CornellBox.yaml",
        "src/main/resources/scenes/EarthScene.yaml",
        "src/main/resources/scenes/PerlinSpheres.yaml",
        "src/main/resources/scenes/QuadsScene.yaml",
        "src/main/resources/scenes/SimpleLightScene.yaml",
        "src/main/resources/scenes/ThreeSpheres.yaml",
        "src/main/resources/scenes/test.yaml"
    )

    val loader = SceneLoader()
    val scene = loader.loadScene(scenes[1])

    val renderer = Renderer(scene, 40, 20)
    val writer = PPMImageWriter()

    val data = renderer.render()
    writer.write(File("test.ppm"), scene.camera, data)
}

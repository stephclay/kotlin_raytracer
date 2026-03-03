package com.wombatsw.raytracing

import com.wombatsw.raytracing.scene.SceneLoader


fun main() {
    val sceneFile = "src/main/resources/scenes/test.yaml"
    val loader = SceneLoader()
    val scene = loader.loadScene(sceneFile)

    println(scene)
}

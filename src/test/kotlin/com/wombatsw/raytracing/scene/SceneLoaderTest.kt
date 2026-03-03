package com.wombatsw.raytracing.scene

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SceneLoaderTest {
    @Test
    fun testLoadCamera() {
        val scene = loadScene("/scenes/cameraTest.yaml")

        assertEquals(NamedRef("camCenter"), scene.camera.cameraCenter)
    }

    @Test
    fun testLoadTriplets() {
        val scene = loadScene("/scenes/tripletTest.yaml")

        assertEquals(1, scene.points.size)
        assertEquals(1, scene.vectors.size)
        assertEquals(1, scene.colors.size)

        assertEquals(TripletDTO(listOf(1.0, 2.0, 3.0)), scene.points["camCenter"])
        assertEquals(TripletDTO(listOf(4.0, 5.0, 6.0)), scene.vectors["up"])
        assertEquals(TripletDTO(listOf(7.0, 8.0, 9.0)), scene.colors["color1"])
    }

    @Test
    fun testLoadTextures() {
        val scene = loadScene("/scenes/textureTest.yaml")
        assertEquals(5, scene.textures.size)

        val blue = TripletDTO(listOf(0.0, 0.0, 1.0))
        assertEquals(SolidColorDTO(InlineRef(blue)), scene.textures["solidBlue"])

        val checker1 = scene.textures["checkerColor"] as CheckerTextureDTO
        val red = TripletDTO(listOf(1.0, 0.0, 0.0))
        val expected1 = CheckerTextureDTO(0.25, null, null, NamedRef("purple"), InlineRef(red))
        assertEquals(expected1, checker1)

        val checker2 = scene.textures["checkerTex"] as CheckerTextureDTO
        val expected2 = CheckerTextureDTO(
            0.5, InlineRef(ImageTextureDTO("xyz.jpg")),
            NamedRef("solidBlue"), null, null
        )
        assertEquals(expected2, checker2)

        assertEquals(ImageTextureDTO("abc.jpg"), scene.textures["image"])

        assertEquals(NoiseTextureDTO(4.0), scene.textures["perlinTex"])
    }

    @Test
    fun testLoadMaterials() {
        val scene = loadScene("/scenes/materialTest.yaml")
        assertEquals(5, scene.materials.size)

        val red = TripletDTO(listOf(1.0, 0.0, 0.0))
        assertEquals(LambertianDTO(InlineRef(red), null), scene.materials["lambertianAlbedo"])

        val green = SolidColorDTO(InlineRef(TripletDTO(listOf(0.0, 1.0, 0.0))))
        assertEquals(LambertianDTO(null, InlineRef(green)), scene.materials["lambertianTexture"])

        assertEquals(DielectricDTO(2.0), scene.materials["dielectric"])

        assertEquals(MetalDTO(NamedRef("blue"), 3.0), scene.materials["metal"])

        val blue = SolidColorDTO(InlineRef(TripletDTO(listOf(0.0, 0.0, 1.0))))
        assertEquals(DiffuseLightDTO(InlineRef(blue)), scene.materials["light"])
    }

    @Test
    fun testLoadWorld() {
        val scene = loadScene("/scenes/worldTest.yaml")
        assertEquals(1, scene.world.size)

        val sphere = scene.world[0] as SphereDTO
        assertEquals(SphereDTO(NamedRef("c1"), 2.0, NamedRef("mat")), sphere)
    }

    fun loadScene(resource: String): SceneDTO {
        val scene: SceneDTO
        javaClass.getResourceAsStream(resource).use {
            val sceneLoader = SceneLoader()
            scene = sceneLoader.loadScene(it!!.reader())
        }

        assertNotNull(scene)
        assertNotNull(scene.camera)
        assertNotNull(scene.world)

        return scene
    }
}
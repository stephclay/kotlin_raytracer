package com.wombatsw.raytracing.model

/**
 * The Scene information
 *
 * @property[camera] The camera
 * @property[world] The list of world objects
 */
data class Scene(val camera: Camera, val world: List<Node>)
package com.wombatsw.raytracing.model

/**
 * Material base class
 */
sealed class Material

/**
 * Lambertian material
 *
 * @property[texture] The texture of the material
 */
data class Lambertian(val texture: Texture) : Material()

/**
 * Dielectric material
 *
 * @property[refractionIndex] The refraction index
 */
data class Dielectric(val refractionIndex: Double) : Material()

/**
 * Metal material
 *
 * @property[albedo] The color of the metal
 * @property[fuzz] The fuzziness factor of the metal
 */
data class Metal(val albedo: Color, val fuzz: Double) : Material()

/**
 * Diffuse Light material
 *
 * @property[texture] The texture of the light source
 */
data class DiffuseLight(val texture: Texture) : Material()

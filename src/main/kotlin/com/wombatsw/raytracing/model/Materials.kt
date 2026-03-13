package com.wombatsw.raytracing.model

import com.wombatsw.raytracing.math.RandomUtils.Companion.randomDouble
import com.wombatsw.raytracing.model.Triplet.Companion.randomUnitVector
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Material scattering information
 *
 * @property[ray] The outgoing scattered ray
 * @property[attenuation] How the material attenuates light when scattered
 */
data class ScatterData(val ray: Ray, val attenuation: Color)

/**
 * Material base class
 */
sealed class Material {
    /**
     * Return the information on how the incoming ray was scattered by this material
     *
     * @param[intersection] The intersection data for the incoming ray
     * @return The scattering information, or null if the light was absorbed
     */
    open fun scatter(intersection: Intersection): ScatterData? {
        return null
    }

    /**
     * Retrieve the color of light emitted by this material
     *
     * @param[intersection] The intersection data for the incoming ray
     * @return The light emission color, or black if the material does not emit light
     */
    open fun emitted(intersection: Intersection): Color = BLACK
}

/**
 * Lambertian material
 *
 * @property[texture] The texture of the material
 */
data class Lambertian(val texture: Texture) : Material() {
    override fun scatter(intersection: Intersection): ScatterData {
        val scatterDir = randomUnitVector() + intersection.n
        val rayDir = if (scatterDir.nearZero()) intersection.n else scatterDir
        return ScatterData(
            Ray(intersection.p, rayDir),
            texture.value(intersection.p, intersection.u, intersection.v)
        )
    }
}

/**
 * Dielectric material
 *
 * @property[refractionIndex] The refraction index
 */
data class Dielectric(val refractionIndex: Double) : Material() {
    override fun scatter(intersection: Intersection): ScatterData {
        val ri = if (intersection.frontFace) 1.0 / refractionIndex else refractionIndex
        val unitDir = intersection.ray.direction.normalize()
        val cosTheta = min(1.0, (-unitDir).dot(intersection.n))
        val sinTheta = sqrt(1.0 - cosTheta * cosTheta)
        val canRefract = ri * sinTheta <= 1.0

        val rayDir = if (canRefract && reflectance(cosTheta, ri) < randomDouble()) {
            unitDir.refract(intersection.n, ri)
        } else {
            unitDir.reflect(intersection.n)
        }

        return ScatterData(
            Ray(intersection.p, rayDir),
            WHITE
        )
    }

    private fun reflectance(cosTheta: Double, ri: Double): Double {
        // Use Schlick's approximation for reflectance.
        val r = (1.0 - ri) / (1.0 + ri)
        val rSq = r * r
        return rSq + (1.0 - rSq) * (1.0 - cosTheta).pow(5.0)
    }
}

/**
 * Metal material
 *
 * @property[albedo] The color of the metal
 * @property[fuzz] The fuzziness factor of the metal
 */
data class Metal(val albedo: Color, val fuzz: Double) : Material() {
    val fuzzAdj = min(1.0, fuzz)

    override fun scatter(intersection: Intersection): ScatterData? {
        var reflected = intersection.ray.direction
            .reflect(intersection.n)
            .normalize()
        if (fuzzAdj > 0) {
            reflected += randomUnitVector() * fuzzAdj
        }
        if (reflected.dot(intersection.n) < 0.0) {
            return null
        }

        return ScatterData(Ray(intersection.p, reflected), albedo)
    }
}

/**
 * Diffuse Light material
 *
 * @property[texture] The texture of the light source
 */
data class DiffuseLight(val texture: Texture) : Material() {
    override fun emitted(intersection: Intersection): Color =
        texture.value(intersection.p, intersection.u, intersection.v)
}

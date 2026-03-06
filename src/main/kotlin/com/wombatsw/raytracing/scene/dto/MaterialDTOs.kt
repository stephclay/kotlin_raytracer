package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.*
import com.wombatsw.raytracing.scene.Ref
import com.wombatsw.raytracing.scene.ResolutionContext
import com.wombatsw.raytracing.scene.Resolvable


/**
 * Material DTO base class
 */
sealed class MaterialDTO : Resolvable<Material>

/**
 * Lambertian DTO
 *
 * @property[albedo] The color [Ref] for a solid texture. Either this or the [texture] property is required
 * @property[texture] The texture [Ref]. Either this or the [albedo] property is required
 */
data class LambertianDTO(val albedo: Ref<TripletDTO>?, val texture: Ref<TextureDTO>?) : MaterialDTO() {
    override fun resolve(ctx: ResolutionContext): Lambertian =
        Lambertian(
            when {
                texture != null -> ctx.resolveTexture(texture)
                albedo != null -> SolidColor(ctx.resolveColor(albedo))
                else -> throw IllegalArgumentException("Either albedo or texture is required")
            }
        )
}

/**
 * Dielectric DTO
 *
 * @property[refractionIndex] The refraction index
 */
data class DielectricDTO(val refractionIndex: Double) : MaterialDTO() {
    override fun resolve(ctx: ResolutionContext): Dielectric = Dielectric(refractionIndex)
}

/**
 * Metal DTO
 *
 * @property[albedo] The color [Ref] for this material
 * @property[fuzz] The fuzziness factor for this material
 */
data class MetalDTO(val albedo: Ref<TripletDTO>, val fuzz: Double) : MaterialDTO() {
    override fun resolve(ctx: ResolutionContext): Metal = Metal(ctx.resolveColor(albedo), fuzz)
}

/**
 * Diffuse Light DTO
 *
 * @property[texture] The texture of the light source
 */
data class DiffuseLightDTO(val texture: Ref<TextureDTO>) : MaterialDTO() {
    override fun resolve(ctx: ResolutionContext): DiffuseLight = DiffuseLight(ctx.resolveTexture(texture))
}

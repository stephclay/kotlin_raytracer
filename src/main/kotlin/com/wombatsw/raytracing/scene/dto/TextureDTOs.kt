package com.wombatsw.raytracing.scene.dto

import com.wombatsw.raytracing.model.*
import com.wombatsw.raytracing.scene.Ref
import com.wombatsw.raytracing.scene.ResolutionContext
import com.wombatsw.raytracing.scene.Resolvable

/**
 * Texture DTO base class
 */
sealed class TextureDTO : Resolvable<Texture>

/**
 * Solid Color DTO
 *
 * @property[color] A reference to the texture color
 */
data class SolidColorDTO(val color: Ref<TripletDTO>) : TextureDTO() {
    override fun resolve(ctx: ResolutionContext): SolidColor = SolidColor(ctx.resolveColor(color))
}

/**
 * Checker Texture DTO. The even and odd textures can be specified either as a texture, or a color which will be
 * converted to a [SolidColor] texture.
 *
 * @property[scale] The scaling factor for the texture
 * @property[even] The even texture [Ref]. Either this or the [evenColor] property is required
 * @property[odd] The odd texture [Ref]. Either this or the [oddColor] property is required
 * @property[evenColor] The color [Ref] for the even solid texture. Either this or the [even] property is required
 * @property[oddColor] The color [Ref] to use for the odd solid texture. Either this or the [odd] property is required
 */
data class CheckerTextureDTO(
    val scale: Double, val even: Ref<TextureDTO>?, val odd: Ref<TextureDTO>?,
    val evenColor: Ref<TripletDTO>?, val oddColor: Ref<TripletDTO>?
) : TextureDTO() {
    override fun resolve(ctx: ResolutionContext): CheckerTexture {
        val evenTex = resolveTexture(ctx, even, evenColor, "even")
        val oddTex = resolveTexture(ctx, odd, oddColor, "odd")
        return CheckerTexture(scale, evenTex, oddTex)
    }

    private fun resolveTexture(
        ctx: ResolutionContext, texture: Ref<TextureDTO>?, color: Ref<TripletDTO>?,
        type: String
    ): Texture =
        when {
            texture != null -> ctx.resolveTexture(texture)
            color != null -> SolidColor(ctx.resolveColor(color))
            else -> throw IllegalArgumentException("Either $type or ${type}Color is required")
        }
}

/**
 * Image texture DTO
 *
 * @property[filename] The image to map onto a surface
 */
data class ImageTextureDTO(val filename: String) : TextureDTO() {
    override fun resolve(ctx: ResolutionContext): ImageTexture = ImageTexture(filename)
}

/**
 * Noise texture DTO
 *
 * @property[scale] The scaling factor for the noise field
 */
data class NoiseTextureDTO(val scale: Double) : TextureDTO() {
    override fun resolve(ctx: ResolutionContext): NoiseTexture = NoiseTexture(scale)
}

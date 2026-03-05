package com.wombatsw.raytracing.model

/**
 * Texture base class
 */
sealed class Texture

/**
 * Solid color texture
 *
 * @property[color] The texture color
 */
data class SolidColor(val color: Color) : Texture()

/**
 * Checker Texture
 *
 * @property[scale] The scaling factor for the texture
 * @property[even] The even texture
 * @property[odd] The odd texture
 */
data class CheckerTexture(val scale: Double, val even: Texture, val odd: Texture) : Texture()

/**
 * Image texture
 *
 * @property[filename] The image to map onto a surface
 */
data class ImageTexture(val filename: String) : Texture()

/**
 * Noise texture
 *
 * @property[scale] The scaling factor for the noise field
 */
data class NoiseTexture(val scale: Double) : Texture()

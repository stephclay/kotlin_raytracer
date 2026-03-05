package com.wombatsw.raytracing.scene

/**
 * Common reference type
 *
 * @param[D] The [Resolvable] DTO
 */
sealed interface Ref<out D : Resolvable<*>>

/**
 * Named reference. Will be resolved after the scene is loaded
 *
 * @param[name] The name of the DTO
 */
data class NamedRef(val name: String) : Ref<Nothing>

/**
 * Inline reference. Contains the value and does not need to be resolved
 *
 * @param[value] The inline DTO value
 * @param[D] The [Resolvable] DTO
 */
data class InlineRef<D : Resolvable<*>>(val value: D) : Ref<D>

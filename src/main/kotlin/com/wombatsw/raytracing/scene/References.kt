package com.wombatsw.raytracing.scene

/**
 * Common reference type
 */
sealed interface Ref<out D>

/**
 * Named reference. Will be resolved after the scene is loaded
 */
data class NamedRef(val name: String) : Ref<Nothing>

/**
 * Inline reference. Contains the value and does not need to be resolved
 */
data class InlineRef<D>(val value: D) : Ref<D>

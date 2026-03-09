package com.wombatsw.raytracing.model

/**
 * Base class for world nodes. Will either be a shape or a bounding container
 */
interface Node {
    /**
     * Get the bounding box inscribing this node
     *
     * @return The [BoundingBox] for this node
     */
    fun boundingBox(): BoundingBox
}
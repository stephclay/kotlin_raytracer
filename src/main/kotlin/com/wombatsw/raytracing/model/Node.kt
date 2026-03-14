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

    /**
     * Get the intersection data for this node, if any
     *
     * @param[ray] The incoming [Ray]
     * @param[tRange] The range if allowed t values for the intersection
     * @return A pair containing the [Intersection] and [Material] data, or null if there was no intersection
     */
    fun intersect(ray: Ray, tRange: Interval): Pair<Intersection, Material>?
}
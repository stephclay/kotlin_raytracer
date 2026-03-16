package com.wombatsw.raytracing.model

/**
 * Bounding Volume
 *
 * @property[nodes] The array of nodes. The root uses the same array as the children
 * @property[start] The start of the array for this node, inclusive
 * @property[end] The start of the array for this node, exclusive
 */
data class BVHNode(val nodes: Array<Node>, val start: Int, val end: Int) : Node {
    /**
     * Root node constructor
     *
     * @param[nodeList] The full list of world nodes
     */
    constructor(nodeList: Collection<Node>) : this(nodeList.toTypedArray(), 0, nodeList.size)

    // Working from an interval is done instead of slicing to avoid creating a new list
    private val bbox: BoundingBox = (start..<end)
        .map { i -> nodes[i].boundingBox() }
        .reduce(BoundingBox::plus)

    val left: Node
    val right: Node
    val numNodes: Int

    init {
        numNodes = end - start
        require(numNodes > 0) { "At least one node is required" }

        val children = when (numNodes) {
            1 -> Pair(nodes[start], nodes[start])
            2 -> Pair(nodes[start], nodes[start + 1])
            else -> {
                val axis = bbox.longestAxis()
                nodes.sortWith(COMPARE_BY_AXIS[axis], start, end)
                val mid = (start + end) ushr 1
                Pair(
                    BVHNode(nodes, start, mid),
                    BVHNode(nodes, mid, end)
                )
            }
        }
        left = children.first
        right = children.second
    }

    override fun boundingBox(): BoundingBox = bbox

    override fun intersect(ray: Ray, tRange: Interval): Pair<Intersection, Material>? {
        val rayInterval = bbox.intersect(ray, tRange) ?: return null

        val leftIntersection = left.intersect(ray, rayInterval)
        if (left === right) {
            return leftIntersection
        }
        val closestT = leftIntersection?.first?.t ?: rayInterval.max

        val rightIntersection = right.intersect(ray, Interval(rayInterval.min, closestT))
        return rightIntersection ?: leftIntersection
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is BVHNode) {
            (left == other.left && right == other.right)
        } else {
            false
        }
    }

    override fun hashCode(): Int = 31 * left.hashCode() + right.hashCode()

    companion object {
        val COMPARE_BY_AXIS = Array(3) { axis ->
            compareBy<Node> { it.boundingBox().axisInterval(axis).min }
        }
    }
}

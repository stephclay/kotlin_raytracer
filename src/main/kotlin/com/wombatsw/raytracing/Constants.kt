package com.wombatsw.raytracing

import com.wombatsw.raytracing.model.Interval

const val EPSILON = 1e-8

/**
 * Similar to a unit interval, but excludes 1.0
 */
val IMG_MAP_INTERVAL = Interval(0, 1.0 - EPSILON)

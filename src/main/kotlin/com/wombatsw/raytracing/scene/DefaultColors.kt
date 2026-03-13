package com.wombatsw.raytracing.scene

import com.wombatsw.raytracing.model.Color

/**
 * Default colors for use in scene descriptions
 */
enum class DefaultColors(r: Number, g: Number, b: Number) {
    RED(1, 0, 0),
    ORANGE(1, 0.647, 0),
    YELLOW(1, 1, 0),
    GREEN(0, 1, 0),
    CYAN(0, 1, 1),
    BLUE(0, 0, 1),
    INDIGO(0.294, 0, 0.51),
    PURPLE(0.616, 0.0, 1.0),
    VIOLET(0.498, 0.0, 1.0),
    MAGENTA(1, 0, 1),
    BLACK(0, 0, 0),
    WHITE(1, 1, 1);

    val color = Color(r, g, b)

    val colorName: String
        get() = name.lowercase()
}
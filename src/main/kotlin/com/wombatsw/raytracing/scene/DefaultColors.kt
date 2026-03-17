package com.wombatsw.raytracing.scene

import com.wombatsw.raytracing.scene.dto.TripletDTO

/**
 * Default colors for use in scene descriptions
 */
object DefaultColors {
    val colorMap = mapOf(
        "red" to TripletDTO(1, 0, 0),
        "orange" to TripletDTO(1, 0.647, 0),
        "yellow" to TripletDTO(1, 1, 0),
        "green" to TripletDTO(0, 1, 0),
        "cyan" to TripletDTO(0, 1, 1),
        "blue" to TripletDTO(0, 0, 1),
        "indigo" to TripletDTO(0.294, 0, 0.51),
        "purple" to TripletDTO(0.616, 0.0, 1.0),
        "violet" to TripletDTO(0.498, 0.0, 1.0),
        "magenta" to TripletDTO(1, 0, 1),
        "black" to TripletDTO(0, 0, 0),
        "white" to TripletDTO(1, 1, 1)
    )
}

package com.wombatsw.raytracing.engine

import com.wombatsw.raytracing.model.Camera
import java.io.File

/**
 * PPM image writer
 */
class PPMImageWriter {
    /**
     * Write an image to the specified file
     *
     * @param[file] the output file
     * @param[camera] the [Camera]
     * @param[data] the raster data
     */
    fun write(file: File, camera: Camera, data: ByteArray) {
        val out = file.outputStream()
        out.use {
            val header = "P6\n${camera.imageWidth} ${camera.imageHeight} 255\n".toByteArray()
            it.write(header)
            it.write(data)
        }
    }
}
package com.wombatsw.raytracing.math

import java.util.concurrent.ThreadLocalRandom

class RandomUtils {
    companion object {
        /**
         * Generate a random integer within the given bounds
         *
         * @param[origin] the least value that can be returned
         * @param[bound] the upper bound (exclusive) for the returned value
         * @return a pseudorandomly chosen value within the given bounds
         */
        fun randomInt(origin: Int, bound: Int): Int =
            ThreadLocalRandom.current().nextInt(origin, bound)

        /**
         * Generate a random double in the range [0, 1)
         *
         * @return a pseudorandomly chosen value within the range 0.0 (inclusive) to 1.0 (exclusive)
         */
        fun randomDouble(): Double =
            ThreadLocalRandom.current().nextDouble()

        /**
         * Generate a random double within the given bounds
         *
         * @param[origin] the least value that can be returned
         * @param[bound] the upper bound (exclusive) for the returned value
         * @return a pseudorandomly chosen value within the given bounds
         */
        fun randomDouble(origin: Double, bound: Double): Double =
            origin + (bound - origin) * randomDouble()

        /**
         * Generate a random double within the given bounds
         *
         * @param[origin] the least value that can be returned
         * @param[bound] the upper bound (exclusive) for the returned value
         * @return a pseudorandomly chosen value within the given bounds
         */
        fun randomDouble(origin: Number, bound: Number): Double =
            randomDouble(origin.toDouble(), bound.toDouble())

        /**
         * Generate a random value within a 2-dimensional unit disc. The distance from the center will be in
         * the range of 0.0 (inclusive) to 1.0 (exclusive)
         *
         * @return A [Pair] containing the X and Y coordinates of the 2d point
         */
        fun randomInUnitDisc(): Pair<Double, Double> {
            while (true) {
                val u = randomDouble()
                val v = randomDouble()
                if (u * u + v * v < 1) {
                    return Pair(u, v)
                }
            }
        }
    }
}
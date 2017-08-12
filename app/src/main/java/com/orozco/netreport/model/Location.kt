package com.orozco.netreport.model

/**
 * @author A-Ar Andrew Concepcion
 * @createdOn 11/08/2017
 */
data class Location(
        val accuracy: Double,
        val altitude: Double,
        val bearing: Double,
        val elapsedRealtimeNanos: Long,
        val latitude: Double,
        val longitude: Double,
        val speed: Double,
        val time: Long,
        val provider: String
)
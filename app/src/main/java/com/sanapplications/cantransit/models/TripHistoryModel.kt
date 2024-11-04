package com.sanapplications.cantransit.models

data class TripHistoryModel (
    val transitName: String = "",
    val fromLocation: String = "",
    val toLocation: String = "",
    val price: Float = 0.0f
)
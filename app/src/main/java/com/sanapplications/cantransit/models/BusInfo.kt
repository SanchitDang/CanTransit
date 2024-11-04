package com.sanapplications.cantransit.models

data class BusInfo(
    val busNumber: String,
    val destination: String,
    val eta: String,
    val status: String
)
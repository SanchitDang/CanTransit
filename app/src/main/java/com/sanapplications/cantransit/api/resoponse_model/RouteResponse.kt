package com.sanapplications.cantransit.api.resoponse_model

import com.squareup.moshi.Json

data class RouteResponse(
    @Json(name = "routes") val routes: List<Route>,
    // Define other fields based on the API response
)

data class Route(
    @Json(name = "legs") val legs: List<Leg>,
    // Define other fields within routes
)

data class Leg(
    @Json(name = "start_address") val startAddress: String,
    @Json(name = "end_address") val endAddress: String,
    @Json(name = "duration") val duration: Duration,
    @Json(name = "distance") val distance: Distance,
    // Define other fields
)

data class Duration(
    @Json(name = "text") val text: String
)

data class Distance(
    @Json(name = "text") val text: String
)

package com.sanapplications.cantransit.api.resoponse_model

import com.squareup.moshi.Json

data class RouteResponse(
    @Json(name = "routes") val routes: List<Route>,
    @Json(name = "status") val status: String
)

data class Route(
    @Json(name = "legs") val legs: List<Leg>,
    @Json(name = "overview_polyline") val overviewPolyline: OverviewPolyline,
    @Json(name = "summary") val summary: String
)

data class Leg(
    @Json(name = "start_address") val startAddress: String,
    @Json(name = "end_address") val endAddress: String,
    @Json(name = "start_location") val startLocation: Location,
    @Json(name = "end_location") val endLocation: Location,
    @Json(name = "duration") val duration: Duration,
    @Json(name = "distance") val distance: Distance,
    @Json(name = "steps") val steps: List<Step>
)

data class Duration(
    @Json(name = "text") val text: String,
    @Json(name = "value") val value: Int // value in seconds
)

data class Distance(
    @Json(name = "text") val text: String,
    @Json(name = "value") val value: Int // value in meters
)

data class OverviewPolyline(
    @Json(name = "points") val points: String
)

data class Location(
    @Json(name = "lat") val lat: Double,
    @Json(name = "lng") val lng: Double
)

data class Step(
    @Json(name = "travel_mode") val travelMode: String,
    @Json(name = "start_location") val startLocation: Location,
    @Json(name = "end_location") val endLocation: Location,
    @Json(name = "polyline") val polyline: Polyline,
    @Json(name = "duration") val duration: Duration,
    @Json(name = "distance") val distance: Distance,
    @Json(name = "html_instructions") val htmlInstructions: String
)

data class Polyline(
    @Json(name = "points") val points: String
)

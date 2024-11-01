package com.sanapplications.cantransit.api.resoponse_model

data class RouteResponse(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)

data class GeocodedWaypoint(
    val geocoder_status: String,
    val place_id: String,
    val types: List<String>
)

data class Route(
    val bounds: Bounds,
    val copyrights: String,
    val legs: List<Leg>,
    val overview_polyline: OverviewPolyline,
    val summary: String,
    val warnings: List<String>,
    val waypoint_order: List<Any?>
)

data class Bounds(
    val northeast: Northeast,
    val southwest: Southwest
)

data class Leg(
    val arrival_time: ArrivalTime,
    val departure_time: DepartureTime,
    val distance: Distance,
    val duration: Duration,
    val end_address: String,
    val end_location: EndLocation,
    val start_address: String,
    val start_location: StartLocation,
    val steps: List<Step>,
    val traffic_speed_entry: List<Any?>,
    val via_waypoint: List<Any?>
)

data class OverviewPolyline(
    val points: String
)

data class Northeast(
    val lat: Double,
    val lng: Double
)

data class Southwest(
    val lat: Double,
    val lng: Double
)

data class ArrivalTime(
    val text: String,
    val time_zone: String,
    val value: Int
)

data class DepartureTime(
    val text: String,
    val time_zone: String,
    val value: Int
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class EndLocation(
    val lat: Double,
    val lng: Double
)

data class StartLocation(
    val lat: Double,
    val lng: Double
)

data class Step(
    val distance: Distance,
    val duration: Duration,
    val end_location: EndLocation,
    val html_instructions: String,
    val polyline: Polyline,
    val start_location: StartLocation,
    val steps: List<StepX>,
    val transit_details: TransitDetails,
    val travel_mode: String
)

data class Polyline(
    val points: String
)

data class StepX(
    val distance: Distance,
    val duration: Duration,
    val end_location: EndLocation,
    val html_instructions: String,
    val maneuver: String,
    val polyline: Polyline,
    val start_location: StartLocation,
    val travel_mode: String
)

data class TransitDetails(
    val arrival_stop: ArrivalStop,
    val arrival_time: ArrivalTime,
    val departure_stop: DepartureStop,
    val departure_time: DepartureTime,
    val headsign: String,
    val line: Line,
    val num_stops: Int
)

data class ArrivalStop(
    val location: Location,
    val name: String
)

data class DepartureStop(
    val location: Location,
    val name: String
)

data class Line(
    val agencies: List<Agency>,
    val color: String,
    val name: String,
    val short_name: String,
    val text_color: String,
    val vehicle: Vehicle
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Agency(
    val name: String,
    val phone: String,
    val url: String
)

data class Vehicle(
    val icon: String,
    val local_icon: String,
    val name: String,
    val type: String
)
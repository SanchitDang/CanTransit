package com.sanapplications.cantransit.screens.trip_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.android.gms.maps.model.LatLng
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.api.repository.RoutesRepository
import com.sanapplications.cantransit.api.resoponse_model.RouteResponse
import kotlinx.coroutines.launch

class TripViewModel : ViewModel() {

    private val repository = RoutesRepository()
    private val _routeState = MutableStateFlow<Result<RouteResponse>?>(null)
    val routeState: StateFlow<Result<RouteResponse>?> = _routeState

    private val _startLocationLatLng = MutableStateFlow(LatLng(43.59428991196505, -79.64704467174485))
    val startLocationLatLng: StateFlow<LatLng> = _startLocationLatLng

    private val _startLocationPlaceId = MutableStateFlow("")
    val startLocationPlaceId: StateFlow<String> = _startLocationPlaceId

    private val _endLocationLatLng = MutableStateFlow(LatLng(43.700110, -79.416300))
    val endLocationLatLng: StateFlow<LatLng> = _endLocationLatLng

    private val _endLocationPlaceId = MutableStateFlow("")
    val endLocationPlaceId: StateFlow<String> = _endLocationPlaceId

    // Function to fetch routes from google directions api
    fun fetchRoute(origin: String, destination: String, context: Context) {
        val mapsApiKey = context.getString(R.string.MAPS_API_KEY)

        viewModelScope.launch {
            val result = repository.fetchRoute(origin, destination, mapsApiKey)
            _routeState.value = result
        }
    }

    // Functions to update the locations
    fun updateStartLocation(newLatLng: LatLng) {
        _startLocationLatLng.value = newLatLng
    }

    fun updateEndLocation(newLatLng: LatLng) {
        _endLocationLatLng.value = newLatLng
    }

    fun updateStartLocationPlaceId(newId: String) {
        _startLocationPlaceId.value = newId
    }

    fun updateEndLocationPlaceId(newId: String) {
        _endLocationPlaceId.value = newId
    }
}

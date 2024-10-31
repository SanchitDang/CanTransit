package com.sanapplications.cantransit.screens.trip_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.android.gms.maps.model.LatLng

class TripViewModel : ViewModel() {

    private val _startLocationLatLng = MutableStateFlow(LatLng(43.59428991196505, -79.64704467174485))
    val startLocationLatLng: StateFlow<LatLng> = _startLocationLatLng

    private val _startLocationPlaceId = MutableStateFlow("")
    val startLocationPlaceId: StateFlow<String> = _startLocationPlaceId

    private val _endLocationLatLng = MutableStateFlow(LatLng(43.700110, -79.416300))
    val endLocationLatLng: StateFlow<LatLng> = _endLocationLatLng

    private val _endLocationPlaceId = MutableStateFlow("")
    val endLocationPlaceId: StateFlow<String> = _endLocationPlaceId

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

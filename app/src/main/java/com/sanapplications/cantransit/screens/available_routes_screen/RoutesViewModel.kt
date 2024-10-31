package com.sanapplications.cantransit.screens.available_routes_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.api.repository.RoutesRepository
import com.sanapplications.cantransit.api.resoponse_model.RouteResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RoutesViewModel : ViewModel() {

    private val repository = RoutesRepository()
    private val _routeState = MutableStateFlow<Result<RouteResponse>?>(null)
    val routeState: StateFlow<Result<RouteResponse>?> = _routeState

    fun fetchRoute(origin: String, destination: String, context: Context) {
        val mapsApiKey = context.getString(R.string.MAPS_API_KEY)
        Log.d("mapsKey", mapsApiKey)

        viewModelScope.launch {
            val result = repository.fetchRoute(origin, destination, mapsApiKey)
            _routeState.value = result
        }
    }
}

package com.sanapplications.cantransit.screens.available_routes_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanapplications.cantransit.api.repository.RoutesRepository
import com.sanapplications.cantransit.api.resoponse_model.RouteResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoutesViewModel(private val repository: RoutesRepository) : ViewModel() {

    private val _routeState = MutableStateFlow<Result<RouteResponse>?>(null)
    val routeState: StateFlow<Result<RouteResponse>?> = _routeState

    fun fetchRoute(origin: String, destination: String, apiKey: String) {
        viewModelScope.launch {
            val result = repository.fetchRoute(origin, destination, apiKey)
            _routeState.value = result
        }
    }
}

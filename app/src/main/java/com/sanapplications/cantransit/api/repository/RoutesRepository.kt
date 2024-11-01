package com.sanapplications.cantransit.api.repository

import com.sanapplications.cantransit.api.RetrofitInstance
import com.sanapplications.cantransit.api.resoponse_model.RouteResponse

class RoutesRepository {

    private val apiService = RetrofitInstance.api

    suspend fun fetchRoute(origin: String, destination: String, apiKey: String): Result<RouteResponse> {
        return try {
            val response = apiService.getRoute(origin, destination,"transit" ,apiKey)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

package com.sanapplications.cantransit.api

import com.sanapplications.cantransit.api.resoponse_model.RouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleRoutesApiService {

    @GET("directions/json")
    suspend fun getRoute(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") apiKey: String
    ): Response<RouteResponse>
}

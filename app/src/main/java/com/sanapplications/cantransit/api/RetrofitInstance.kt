package com.sanapplications.cantransit.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    val api: GoogleRoutesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GoogleRoutesApiService::class.java)
    }
}

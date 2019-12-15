package com.example.carservice.service

import com.example.carservice.item.PlacemarksResponse
import retrofit2.Response
import retrofit2.http.*


interface CarServiceApi {

    @GET("locations.json")
    suspend fun getPlacemarks(): Response<PlacemarksResponse>


}
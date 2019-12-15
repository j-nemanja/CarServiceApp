package com.example.carservice.service


class RemoteRepository {

    private var client: CarServiceApi = RetrofitService().carServiceApi

    suspend fun getPlacemarks() = client.getPlacemarks()

}
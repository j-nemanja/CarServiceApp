package com.example.carservice.item

import com.google.gson.annotations.SerializedName

data class PlacemarksResponse (
    @SerializedName("placemarks")
    val placemarks : List<Placemark>
)

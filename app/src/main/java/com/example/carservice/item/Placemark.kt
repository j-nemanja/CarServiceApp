package com.example.carservice.item

data class Placemark (
    val address : String,
    val coordinates : List<Double>,
    val engineType : String,
    val exterior : String,
    val fuel : Int,
    val interior : String,
    val name : String,
    val vin : String
)
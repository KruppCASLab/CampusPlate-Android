package com.example.modernization.model.types


data class FoodStop(
    val id: String,
    val name: String,
    val foodStopNumber: Int,
    val description: String,
    val lat: Double,
    val lng: Double,
    val hexColor: String
)

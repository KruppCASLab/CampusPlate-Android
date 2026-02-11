package com.example.modernization.model.requests

data class CreateListingRequest(
    val foodStopId: Int,
    val title: String,
    val description: String,
    val quantity: Int
)
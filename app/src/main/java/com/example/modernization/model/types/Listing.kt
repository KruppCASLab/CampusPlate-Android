package com.example.modernization.model.types

data class Listing(
    val listingId: Int,
    val foodStopId: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val creationTime: Long,
    val expirationTime: Long,
    val quantity: Int,
    val weightOunces: Int,
    val image: String?,            // nullable because it can be null
    val quantityRemaining: Int
)
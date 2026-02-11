package com.example.modernization.model.types


class Reservation(
    var listingId: Int = -1,
    var quantity: Int = 0,
    var userId: Int = -1,
    var status: Int = 0,
    var code: Int = 0,
    var timeCreated: Long = 0L,
    var timeExpired: Long = 0L,
    var reservationId: Int = -1,
    var listing: Listing? = null,
    var lat: Double = 0.0,
    var lng: Double = 0.0
)

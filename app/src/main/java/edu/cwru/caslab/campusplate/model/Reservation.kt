package edu.cwru.caslab.campusplate.model

data class Reservation(
  val reservationId: Int,
  val userId: Int,
  val listingId: Int,
  val quantity: Int,
  val status: Int,
  val code: Int,
  val timeCreated: Long,
  val timeExpired: Long
)

data class ReservationPostResponse(
  val data: Reservation,
  val status: Int,
  val error: String?
)

data class ReservationRequest(
  val listingId: Int,
  val quanitity: Int
)

data class ReservationGetResponse(
  val data: List<Reservation>,
  val status: Int,
  val error: String?
)

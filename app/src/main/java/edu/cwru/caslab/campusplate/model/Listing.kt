package edu.cwru.caslab.campusplate.model

data class Listing(
  val listingId: Int,
  val foodStopId: Int,
  val userId: Int,
  val title: String,
  val description: String,
  val creationTime: Int,
  val expirationTime: Int,
  val quantity: Int,
  val weightOunces: Int,
  val quantityRemaining: String,
  val image: String?
)

data class ListingResponse(
  val data: List<Listing>,
  val status: Int,
  val error: String
)

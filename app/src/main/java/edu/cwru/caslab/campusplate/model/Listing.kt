package edu.cwru.caslab.campusplate.model

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
  val quantityRemaining: String,
  val image: String?
)

data class ListingResponse(
  val data: List<Listing>,
  val status: Int,
  val error: String
)

data class ListingCreationRequest(
  val foodStopId: Int,
  val title: String,
  val description: String,
  val quantity: Int,
  val weightOunces: Int,
  val creationDate: Long,
  val expirationDate: Long,
  val image: String?
)

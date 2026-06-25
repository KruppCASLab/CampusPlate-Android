package edu.cwru.caslab.campusplate.model

data class FoodStop(
  val foodStopId: Int,
  val name: String,
  val description: String,
  val streetAddress: String,
  val lat: Double,
  val lng: Double,
  val hexColor: String,
  val foodStopNumber: Int,
  val managed: Int,
  val reservable: Int
)

data class FoodStopResponse(
  val data: List<FoodStop>,
  val status: Int,
  val error: String
)

package edu.cwru.caslab.campusplate.model

data class Pin (
  val pin: String
)

data class PinResponse (
  val data: Guid,
  val status: Int,
  val error: String
)

data class Guid (
  val GUID: String
)

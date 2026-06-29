package edu.cwru.caslab.campusplate.model

data class GenericResponse(
  val data: String,
  val status: Int,
  val error: String?
)

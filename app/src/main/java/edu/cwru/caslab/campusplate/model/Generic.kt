package edu.cwru.caslab.campusplate.model

// NOTE: This cannot be abstract or retrofit throws a fit
//  pun intended :)
open class GenericResponse<T>(
  val data: T,
  val status: Int,
  val error: String?
)

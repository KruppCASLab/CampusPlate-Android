package edu.cwru.caslab.campusplate.model

data class User(
  val userName: String,
  val credential: Credential
)

data class UserResponse(
  val data: String,
  val status: Int,
  val error: String
)

data class Credential(
  val label: String
)

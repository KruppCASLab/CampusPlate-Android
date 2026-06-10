package edu.cwru.caslab.campusplate.model

data class User(
  val userName: String,
  val credential: Credential
)

data class Credential(
  val label: String
)

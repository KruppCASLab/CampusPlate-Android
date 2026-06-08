package edu.cwru.caslab.campusplate.network

data class User(
  val userName: String,
  val credential: Credential
)

data class Credential(
  val label: String
)

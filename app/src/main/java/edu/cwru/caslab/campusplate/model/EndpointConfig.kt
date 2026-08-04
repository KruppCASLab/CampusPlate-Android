package edu.cwru.caslab.campusplate.model

import kotlinx.serialization.Serializable

@Serializable
data class EndpointConfig(
  val domain: String,
  val production: String,
  val testing: String
)

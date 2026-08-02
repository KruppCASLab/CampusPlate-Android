package edu.cwru.caslab.campusplate.model

data class ProductResponse(
  val code: String,
  val product: Product,
  val status: Int,
  val status_verbose: String
)

data class Product(
  val allergens: List<String>,
  val brands: String,
  val categories: String,
  val image_url: String
)

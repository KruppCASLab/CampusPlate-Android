package edu.cwru.caslab.campusplate.network

import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.model.GenericResponse
import edu.cwru.caslab.campusplate.model.Guid
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.model.ListingCreationRequest
import edu.cwru.caslab.campusplate.model.Pin
import edu.cwru.caslab.campusplate.model.ProductResponse
import edu.cwru.caslab.campusplate.model.Reservation
import edu.cwru.caslab.campusplate.model.ReservationRequest
import edu.cwru.caslab.campusplate.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

// TODO: Configure based on domain
private const val BASE_URL = "https://world.openfoodfacts.org/api/v0/"
private val retrofit = Retrofit.Builder()
  .baseUrl(BASE_URL)
  .addConverterFactory(GsonConverterFactory.create())
  .build()

object OpenFoodFactsApi {
  val retrofitService : OpenFoodFactsApiService by lazy {
    retrofit.create(OpenFoodFactsApiService::class.java)
  }
}

interface OpenFoodFactsApiService {

  @POST("product/{id}")
  suspend fun getProduct(@Path("id") id: String?): Response<ProductResponse>

} 

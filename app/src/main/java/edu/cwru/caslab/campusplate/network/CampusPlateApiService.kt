package edu.cwru.caslab.campusplate.network

import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.model.GenericResponse
import edu.cwru.caslab.campusplate.model.Guid
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.model.ListingCreationRequest
import edu.cwru.caslab.campusplate.model.Pin
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
private const val BASE_URL = "https://caslab.case.edu/~briankrupp/rest.php/"
private val retrofit = Retrofit.Builder()
  .baseUrl(BASE_URL)
  .addConverterFactory(GsonConverterFactory.create())
  .build()

object CampusPlateApi {
  val retrofitService : CampusPlateApiService by lazy {
    retrofit.create(CampusPlateApiService::class.java)
  }
}

interface CampusPlateApiService {

  @POST("users")
  suspend fun createUser(@Body user: User): Response<GenericResponse<String>>

  @PATCH("users/{id}")
  suspend fun validatePin(@Path("id") id: String?, @Body pin: Pin): Response<GenericResponse<Guid>>

  @GET("listings")
  suspend fun getListings(@Header("Authorization") authorization: String): Response<GenericResponse<List<Listing>>> 

  @GET("listings/{id}/image")
  suspend fun getListingImage(@Header("Authorization") authorization: String, @Path("id") id: String?): Response<GenericResponse<String>> 
  
  @POST("listings")
  suspend fun createListing(@Header("Authorization") authorization: String, @Body listingCreationRequest: ListingCreationRequest): Response<GenericResponse<String>>

  @GET("foodstops")
  suspend fun getFoodStops(@Header("Authorization") authorization: String): Response<GenericResponse<List<FoodStop>>>

  @GET("foodstops/manage")
  suspend fun getManagedFoodStops(@Header("Authorization") authorization: String): Response<GenericResponse<List<FoodStop>>>

  @POST("reservations")
  suspend fun createReservation(@Header("Authorization") authorization: String, @Body reservationRequest: ReservationRequest): Response<GenericResponse<Reservation>>

  @GET("reservations")
  suspend fun getReservations(@Header("Authorization") authorization: String): Response<GenericResponse<List<Reservation>>>


}

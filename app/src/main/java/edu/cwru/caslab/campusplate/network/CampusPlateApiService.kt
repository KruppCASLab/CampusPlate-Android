package edu.cwru.caslab.campusplate.network

import edu.cwru.caslab.campusplate.model.ListingResponse
import edu.cwru.caslab.campusplate.model.Pin
import edu.cwru.caslab.campusplate.model.PinResponse
import edu.cwru.caslab.campusplate.model.User
import edu.cwru.caslab.campusplate.model.UserResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Url

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
  suspend fun createUser(@Body user: User): Response<UserResponse>

  @PATCH("users/{id}")
  suspend fun validatePin(@Path("id") id: String?, @Body pin: Pin): Response<PinResponse>

  @GET("listings")
  suspend fun getListings(@Header("Authorization") authorization: String): Response<ListingResponse> 
}

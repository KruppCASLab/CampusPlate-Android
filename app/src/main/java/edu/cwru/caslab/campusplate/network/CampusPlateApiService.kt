package edu.cwru.caslab.campusplate.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body


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
  suspend fun createUser(@Body user: User): Response<User>

  @PATCH
  fun validatePin(): String
}

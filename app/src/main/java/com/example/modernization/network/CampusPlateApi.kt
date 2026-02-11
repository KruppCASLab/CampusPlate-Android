package com.example.modernization.network

import com.example.modernization.model.requests.AuthStartRequest
import com.example.modernization.model.requests.ConfirmPinRequest
import com.example.modernization.model.requests.CreateUserRequest
import com.example.modernization.model.types.*
import com.example.modernization.model.responses.ApiResponse
import com.example.modernization.model.requests.CreateListingRequest


import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface CampusPlateApi {

    // ----------------------------
    // User Creation and Confirmation
    // ----------------------------

    // Postman: POST Create User
    @POST("users")
    suspend fun createUser(
        @Body request: CreateUserRequest
    ): Response<ApiResponse<User>>

    // Postman: POST Email Pin
    @POST
    suspend fun emailPin(
        @Body request: AuthStartRequest
    ): Response<ApiResponse<Any>>

    // Postman: PATCH Confirm Pin
    @PATCH("users/{email}")
    suspend fun confirmPin(
        @Path("email") email: String,
        @Body request: ConfirmPinRequest
    ): Response<ApiResponse<User>>


    // ----------------------------
    // Listings
    // ----------------------------

    // Postman: GET Get Listings
    @GET("listings")
    suspend fun getListings(): Response<ApiResponse<List<Listing>>>

    // Postman: GET Get Listings For Food Stop
    @GET("listings")
    suspend fun getListingsForFoodStop(
        @Query("foodStopId") foodStopId: Int
    ): Response<ApiResponse<List<Listing>>>



    // Postman: GET Get Listings Image
    // Usually returns bytes. Use @Streaming so it doesn't load whole image into memory.
    @Streaming
    @GET("listings/{listingId}/image")
    suspend fun getListingImage(
        @Path("listingId") listingId: Int
    ): Response<ResponseBody>

    // Postman: POST Create Listing - No Image
    @POST("listings")
    suspend fun createListingNoImage(
        @Body request: CreateListingRequest
    ): Response<ApiResponse<Listing>>

    // Postman: POST Create Listing - with Image (if your API supports it)
    // If your backend is multipart upload, use this.
    @Multipart
    @POST("listings")
    suspend fun createListingWithImage(
        @Part("foodStopId") foodStopId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ApiResponse<Listing>>


    // ----------------------------
    // FoodStops
    // ----------------------------

    // Postman: POST Create Food Stop
    @POST("foodstops")
    suspend fun createFoodStop(
        @Body request: FoodStop
    ): Response<ApiResponse<FoodStop>>

    // Postman: GET Get Food Stops
    @GET("foodstops")
    suspend fun getFoodStops(): Response<ApiResponse<List<FoodStop>>>


    // Postman: GET Get Food Stops Managed
    @GET("foodstops/manage")
    suspend fun getFoodStopsManaged(): Response<ApiResponse<List<FoodStop>>>


    // ----------------------------
    // Reservations
    // ----------------------------

    // Postman: POST Create Reservation
    @POST("reservations")
    suspend fun createReservation(
        @Body request: Reservation
    ): Response<ApiResponse<Reservation>>

    // Postman: GET Get Reservations
    @GET("reservations")
    suspend fun getReservations(): Response<ApiResponse<List<Reservation>>>

    // Postman: DEL Delete Reservation
    @DELETE("reservations")
    suspend fun deleteReservation(
        @Query("reservationId") reservationId: Int
    ): Response<ApiResponse<Any>>


    // ----------------------------
    // Admin Specific
    // ----------------------------

    // Postman: GET Get All Users
    @GET("users/all")
    suspend fun getAllUsers(): Response<ApiResponse<List<User>>>

    // Postman: GET Get All Managers
    @GET("users/all_managers")
    suspend fun getAllManagers(): Response<ApiResponse<List<User>>>


}
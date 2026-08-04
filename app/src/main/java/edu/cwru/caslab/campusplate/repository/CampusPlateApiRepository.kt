package edu.cwru.caslab.campusplate.repository

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
import retrofit2.converter.gson.GsonConverterFactory

class CampusPlateApiRepository(
    private val endpointResolver: EndpointResolver,
    private val factory: CampusPlateApiServiceFactory = CampusPlateApiServiceFactory()
) {
    private val cache = mutableMapOf<String, CampusPlateApiService>()
    private val defualtTestEndpointOverride = true

    private fun apiFor(
        email: String,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): CampusPlateApiService {
        val baseUrl = endpointResolver.resolve(email, useTestEndpoint)
        return cache.getOrPut(baseUrl) {
            factory.create(baseUrl)
        }
    }

    suspend fun createUser(
        user: User,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<String>> {
        return apiFor(user.userName, useTestEndpoint).createUser(
            user = user
        )
    }

    suspend fun validatePin(
        email: String,
        id: String?,
        pin: Pin,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<Guid>> {
        return apiFor(email, useTestEndpoint).validatePin(
            id = id,
            pin = pin
        )
    }

    suspend fun getListings(
        email: String,
        authorization: String,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<List<Listing>>> {
        return apiFor(email, useTestEndpoint).getListings(
            authorization = authorization
        )
    }

    suspend fun getListingImage(
        email: String,
        authorization: String,
        id: String?,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<String>> {
        return apiFor(email, useTestEndpoint).getListingImage(
            authorization = authorization,
            id = id
        )
    }

    suspend fun createListing(
        email: String,
        authorization: String,
        listingCreationRequest: ListingCreationRequest,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<String>> {
        return apiFor(email, useTestEndpoint).createListing(
            authorization = authorization,
            listingCreationRequest = listingCreationRequest
        )
    }

    suspend fun getFoodStops(
        email: String,
        authorization: String,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<List<FoodStop>>> {
        return apiFor(email, useTestEndpoint).getFoodStops(
            authorization = authorization
        )
    }

    suspend fun getManagedFoodStops(
        email: String,
        authorization: String,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<List<FoodStop>>> {
        return apiFor(email, useTestEndpoint).getManagedFoodStops(
            authorization = authorization
        )
    }

    suspend fun createReservation(
        email: String,
        authorization: String,
        reservationRequest: ReservationRequest,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<Reservation>> {
        return apiFor(email, useTestEndpoint).createReservation(
            authorization = authorization,
            reservationRequest = reservationRequest
        )
    }

    suspend fun getReservations(
        email: String,
        authorization: String,
        useTestEndpoint: Boolean = defualtTestEndpointOverride
    ): Response<GenericResponse<List<Reservation>>> {
        return apiFor(email, useTestEndpoint).getReservations(
            authorization = authorization
        )
    }
}


class CampusPlateApiServiceFactory {
    fun create(baseUrl: String): CampusPlateApiService {

        val normalized = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

        return Retrofit.Builder()
            .baseUrl(normalized)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CampusPlateApiService::class.java)
    }
}

interface EndpointResolver {
    fun resolve(email: String, useTestEndpoint: Boolean = false): String
}



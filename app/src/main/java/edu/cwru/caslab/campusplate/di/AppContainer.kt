package edu.cwru.caslab.campusplate.di

import android.content.Context
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.data.local.dataStore
import edu.cwru.caslab.campusplate.repository.CampusPlateApiRepository
import edu.cwru.caslab.campusplate.repository.CampusPlateApiServiceFactory
import edu.cwru.caslab.campusplate.repository.CampusPlateEndpointResolver
import edu.cwru.caslab.campusplate.repository.EndpointRepository
import edu.cwru.caslab.campusplate.repository.StoredCredentialRepository
import edu.cwru.caslab.campusplate.security.KeystoreCryptographer

class AppContainer(
    context: Context
) {
    private val appContext = context.applicationContext

    val endpointConfigRepository: EndpointRepository by lazy {
        EndpointRepository(appContext)
    }

    val endpointResolver: CampusPlateEndpointResolver by lazy {
        CampusPlateEndpointResolver(endpointConfigRepository)
    }

    val storedCredentialRepository: StoredCredentialRepository by lazy {
      StoredCredentialRepository(
        dataStore =  appContext.dataStore,
        cryptographer = cryptographer
      )
    }
    
    val cryptographer: KeystoreCryptographer by lazy {
        KeystoreCryptographer()
    } 

    val apiServiceFactory: CampusPlateApiServiceFactory by lazy {
        CampusPlateApiServiceFactory()
    }

    val apiRepository: CampusPlateApiRepository by lazy {
        CampusPlateApiRepository(
            endpointResolver = endpointResolver,
            factory = apiServiceFactory
        )
    }
}

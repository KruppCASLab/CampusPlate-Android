package edu.cwru.caslab.campusplate.repository

import edu.cwru.caslab.campusplate.BuildConfig
import edu.cwru.caslab.campusplate.model.EndpointConfig

class CampusPlateEndpointResolver(
    private val configRepository: EndpointRepository
): EndpointResolver {
    private val config by lazy { configRepository.getEndpoints() }

    override fun resolve(email: String, useTestEndpoint: Boolean): String {

        val domainMapping = mutableMapOf<String, EndpointConfig>()

        config.forEach { domainMapping[it.domain] = it }

        val domain = email.substringAfter("@", "").lowercase() 


        var mapping = if (useTestEndpoint && BuildConfig.ALLOW_TEST_ENDPOINT)
          domainMapping[domain]?.testing
        else 
          domainMapping[domain]?.production

        if (mapping != null) return mapping else throw IllegalArgumentException("No such domain")
    }
}

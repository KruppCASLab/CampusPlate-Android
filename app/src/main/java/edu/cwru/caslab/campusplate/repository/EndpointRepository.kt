package edu.cwru.caslab.campusplate.repository

import android.content.Context
import edu.cwru.caslab.campusplate.model.EndpointConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer

class EndpointRepository(
    private val context: Context
) {
    fun getEndpoints(): List<EndpointConfig> {
        val jsonString = context.assets.open("endpoints.json")
            .bufferedReader()
            .use { it.readText() }

        return Json.decodeFromString(
            ListSerializer(EndpointConfig.serializer()),
            jsonString
        )
    }
}


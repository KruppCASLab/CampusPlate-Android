package edu.cwru.caslab.campusplate.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class StoredCredentialRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val CREDENTIAL_KEY = stringPreferencesKey("credential")

    val credential: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                // Treat IO failure as no credential existing
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[CREDENTIAL_KEY]
        }

    suspend fun saveCredential(credential: String) {
        dataStore.edit { preferences ->
            preferences[CREDENTIAL_KEY] = credential
        }
    }

    suspend fun clearCredential() {
        dataStore.edit { preferences ->
            preferences.remove(CREDENTIAL_KEY)
        }
    }
}
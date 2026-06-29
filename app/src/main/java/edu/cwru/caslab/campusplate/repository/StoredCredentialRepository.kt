package edu.cwru.caslab.campusplate.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import edu.cwru.caslab.campusplate.security.Cryptographer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

data class StoredCredential(val email: String?, val credential: String?)

class StoredCredentialRepository(
    private val dataStore: DataStore<Preferences>,
    private val cryptographer: Cryptographer
) {
    private val CREDENTIAL_KEY = stringPreferencesKey("credential")
    private val EMAIL_KEY = stringPreferencesKey("email")

    val storedCredential: Flow<StoredCredential> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                // Treat IO failure as no credential existing
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Decryption failure is treated as no credentials, make user log in again
            val decrypted = preferences[CREDENTIAL_KEY]?.let {
                try {
                    cryptographer.decrypt(it)
                } catch (e: Exception) {
                    null
                }
            }
            StoredCredential(
                email = if (decrypted == null) null else preferences[EMAIL_KEY],
                credential = decrypted
            )
        }

    suspend fun saveCredential(email: String, credential: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
            preferences[CREDENTIAL_KEY] = cryptographer.encrypt(credential)
        }
    }

    suspend fun clearCredential() {
        dataStore.edit { preferences ->
            preferences.remove(CREDENTIAL_KEY)
            preferences.remove(EMAIL_KEY)
        }
    }
}
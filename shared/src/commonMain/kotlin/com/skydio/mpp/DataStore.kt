package com.skydio.mpp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.internal.synchronized
import okio.Path.Companion.toPath
import kotlin.concurrent.Volatile


fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "mpp.preferences_pb"
val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
val AUTH_REFRESH_KEY = stringPreferencesKey("refresh_token")

expect object DataStoreMaker {
    fun make(): DataStore<Preferences>
}

object DataStoreSingleton {
    private lateinit var dataStore: DataStore<Preferences>

    fun getDataStore(): DataStore<Preferences> {
        return if (::dataStore.isInitialized) {
            dataStore
        } else {
            DataStoreMaker.make().also {
                this.dataStore = it
            }
        }
    }
}



package com.skydio.mpp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import okio.Path.Companion.toPath


fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "mpp.preferences_pb"
val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

expect object DataStoreMaker {
    fun make(): DataStore<Preferences>
}


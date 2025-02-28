package com.skydio.mpp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)

val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

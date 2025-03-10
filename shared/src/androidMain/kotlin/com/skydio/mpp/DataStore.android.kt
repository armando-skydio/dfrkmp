package com.skydio.mpp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(): DataStore<Preferences> {
    val context = AppContext.get()
    return createDataStore(
        producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
    )
}

actual object DataStoreMaker {
    actual fun make(): DataStore<Preferences> {
        return createDataStore()
    }
}

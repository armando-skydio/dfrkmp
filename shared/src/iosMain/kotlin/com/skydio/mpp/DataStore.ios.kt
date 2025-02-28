package com.skydio.mpp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual object DataStoreMaker {
    actual fun make(): DataStore<Preferences> {
        return createDataStore()
    }
}

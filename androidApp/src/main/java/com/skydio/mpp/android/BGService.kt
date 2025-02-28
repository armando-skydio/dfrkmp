package com.skydio.mpp.android

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.skydio.mpp.AUTH_REFRESH_KEY
import com.skydio.mpp.AUTH_TOKEN_KEY
import com.skydio.mpp.DataStoreMaker
import com.skydio.mpp.LocationData
import com.skydio.mpp.LocationTracker
import com.skydio.mpp.SkyLocationListener
import com.skydio.mpp.login.API
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class BGService: Service() {

    private val locationTracker: LocationTracker = LocationTracker()

    private val tokenFlow: Flow<String> = DataStoreMaker.make().data.map { preferences ->
        preferences[AUTH_TOKEN_KEY] ?: ""
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        locationTracker.startTracking()
        locationTracker.addLocationListener(object : SkyLocationListener {
            override fun onNewLocation(data: LocationData) {
                Log.d("PatrolLink", "Location: $data")
                updateCapacitor(null, data)
            }
        })

        refreshToken()

        GlobalScope.launch {
            tokenFlow.collect {
                if (it.isNotEmpty()) {
                    Log.d("PatrolLink", "Token: $it")
                    updateCapacitor(it, null)
                }
            }
        }
    }

    private fun updateCapacitor(token: String?, locationData: LocationData?) {
        val intent = Intent("token")
        token?.let {
            intent.putExtra("token", token)
        }
        locationData?.let {
            intent.putExtra("latitude", locationData.latitude)
            intent.putExtra("longitude", locationData.longitude)
            intent.putExtra("altitude", locationData.altitude)
            intent.putExtra("speed", locationData.speed)
            intent.putExtra("bearing", locationData.bearing)
            intent.putExtra("accuracy", locationData.accuracy)
        }
        sendBroadcast(intent)
    }

    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        private fun refreshToken() = GlobalScope.launch {
            while (true) {
                val ds = DataStoreMaker.make()
                println("Refreshing token")
                val api = API()
                val refreshToken = ds.data.map { preferences ->
                    preferences[AUTH_REFRESH_KEY] ?: ""
                }.first()
                if (refreshToken.isEmpty()) {
                    continue
                }
                val refreshedToken = api.refreshToken(refreshToken)
                refreshedToken?.let { refreshResponse ->
                    refreshResponse.accessToken
                    ds.edit {
                        it[AUTH_TOKEN_KEY] = refreshResponse.accessToken
                    }
                    println(refreshResponse)
                } ?: kotlin.run {
                    println("Refresh token failed")
                }
                delay(5.seconds)
            }
        }
    }

}

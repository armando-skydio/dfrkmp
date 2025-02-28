package com.skydio.mpp.repository

import com.skydio.mpp.api.SkydioApi

class WaypointsRepository(private val service: SkydioApi)  {

    suspend fun getMarkersByOrgId(id: String) = service.waypointMissionQuery(id)
}
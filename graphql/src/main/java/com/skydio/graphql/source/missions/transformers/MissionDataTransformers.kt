package com.skydio.graphql.source.missions.transformers

import android.content.Context
import com.google.protobuf.util.JsonFormat
import com.skydio.djinni.mvvm.MapPolygon
import com.skydio.djinni.mvvm.MissionFileData
import com.skydio.djinni.mvvm.WorkflowStat
import com.skydio.djinni.records.MapCoordinate
import com.skydio.djinni.records.MapMarker
import com.skydio.graphql.WaypointMissionQuery
import skills.MissionOuterClass.Mission
import java.time.Instant

class MissionDataTransformer(private val edge: WaypointMissionQuery.Edge, private val uid: String) {

    operator fun invoke(): MissionFileData? {

        val builder = MissionDataBuilder()
        val polygonBuilder = MapPolygonBuilder()

        edge.node?.currentSpec?.json?.also { json ->
            val mission = Mission.newBuilder()
            val cleaned = json.replace("\\\\", "")
            JsonFormat.parser().ignoringUnknownFields().merge(cleaned, mission)
            builder.name = mission.displayName
            polygonBuilder.id = uid
            mission.actionsList.firstOrNull()?.let { rootMissionAction ->
                rootMissionAction.args.sequence.actionsList.forEach { action2 ->
                    if (action2.args.hasSurfaceScanSkill().not()) {
                        return null
                    }
                    builder.uTime = action2.args.surfaceScanSkill.saveUclock
                    action2.args.surfaceScanSkill.inspectionParameters.gpsHelper.gpsPolygonList?.let { gpsPolygons ->
                        for (gpsPolygon in gpsPolygons) {
                            val mapMarker = MapMarkerBuilder().apply {
                                id = gpsPolygon.key.toString()
                                //label = gpsPolygon.key

                                heading = gpsPolygon.heading.toDouble()
                                heading = if (gpsPolygon.hasHeading) {
                                    gpsPolygon.heading.toDouble()
                                } else {
                                    null
                                }
                                gimbalPitch = if (gpsPolygon.hasGimbalPitch) {
                                    gpsPolygon.gimbalPitch.toDouble()
                                } else {
                                    null
                                }
                                mapCoordinate = MapCoordinate(
                                    gpsPolygon.latitude,
                                    gpsPolygon.longitude,
                                    if (gpsPolygon.hasAltitude) gpsPolygon.altitude else null
                                )
                                //dconHash = waypointAction.actionUuid
                            }.invoke()
                            if (mapMarker.coordinate.latitude == 0.0 && mapMarker.coordinate.latitude == 0.0) continue
                            polygonBuilder.mapMarkers.add(mapMarker)
                        }
                    }
                }
            }
        }
        if (polygonBuilder.mapMarkers.isNotEmpty()) {
            builder.mapPolygon = polygonBuilder.invoke()
            builder.filePath = uid
            return builder.invoke()
        }
        return null
    }

}

class MissionDataTransformerFromFile(private val mission: Mission, private val uid: String) {

    operator fun invoke(): MissionFileData {

        val builder = MissionDataBuilder()
        val polygonBuilder = MapPolygonBuilder()

        builder.name = mission.displayName
        builder.uTime = mission.utime
        polygonBuilder.id = uid
        mission.actionsList.firstOrNull()?.let { rootMissionAction ->
            rootMissionAction.args.sequence.actionsList.forEach { action2 ->
                if (action2.args.hasSurfaceScanSkill().not()) {
                    return builder.invoke()
                }
                builder.uTime = action2.args.surfaceScanSkill.saveUclock
                action2.args.surfaceScanSkill.inspectionParameters.gpsHelper.gpsPolygonList?.let { gpsPolygons ->
                    for (gpsPolygon in gpsPolygons) {
                        val mapMarker = MapMarkerBuilder().apply {
                            id = gpsPolygon.key.toString()

                            heading = gpsPolygon.heading.toDouble()
                            heading = if (gpsPolygon.hasHeading) {
                                gpsPolygon.heading.toDouble()
                            } else {
                                null
                            }
                            gimbalPitch = if (gpsPolygon.hasGimbalPitch) {
                                gpsPolygon.gimbalPitch.toDouble()
                            } else {
                                null
                            }
                            mapCoordinate = MapCoordinate(
                                gpsPolygon.latitude,
                                gpsPolygon.longitude,
                                if (gpsPolygon.hasAltitude) gpsPolygon.altitude else null
                            )
                        }.invoke()
                        if (mapMarker.coordinate.latitude == 0.0 && mapMarker.coordinate.latitude == 0.0) continue
                        polygonBuilder.mapMarkers.add(mapMarker)
                    }
                }
            }
        }
        if (polygonBuilder.mapMarkers.isNotEmpty()) {
            builder.mapPolygon = polygonBuilder.invoke()
            builder.filePath = uid
            return builder.invoke()
        }
        return builder.invoke()
    }

}

internal class MissionDataBuilder {

    var name: String = ""
    var uTime: Long = 0
    val workFlowStat: ArrayList<WorkflowStat> = arrayListOf()
    var filePath: String = ""
    var scanModeLabel: String = ""
    var mapPolygon: MapPolygon? = null

    operator fun invoke(): MissionFileData {
        val polygon = checkNotNull(mapPolygon) {
            "MissionTransformer: MapPolygon is null"
        }
        return MissionFileData(
            /* missionName = */ name,
            /* polygon = */ polygon,
            /* uclock = */ uTime,
            /* statView = */ workFlowStat,
            /* filePath = */ filePath,
            /* scanModeLabel = */ scanModeLabel
        )
    }
}

internal class MapPolygonBuilder {

    var id: String = ""
    val mapMarkers: ArrayList<MapMarker> = arrayListOf()

    operator fun invoke(): MapPolygon {
        return MapPolygon(
            /* id = */ id,
            /* markers = */ mapMarkers,
            /* strokeWidth = */ 2,
            /* strokeColor = */ -1,
            /* fillColor = */ 16777215,
            /* isGrowableAndShrinkable = */ false,
            /* isLoop = */ false,
            /* lineSortKey = */ 0
        )
    }
}

internal class MapMarkerBuilder {

    var id: String = ""
    var label: String = ""
    var dconHash: String = ""
    var mapCoordinate: MapCoordinate? = null
    var heading: Double? = null
    var gimbalPitch: Double? = null

    operator fun invoke(): MapMarker {
        val coordinate = checkNotNull(mapCoordinate) {
            "MissionTransformer: mapCoordinate is null"
        }
        return MapMarker(
            /* id = */ id,
            /* label = */ label,
            /* coordinate = */ coordinate,
            /* heading = */ heading,
            /* gimbalPitch = */ gimbalPitch,
            /* dconHash = */ dconHash,
            /* zIndex = */ 0,
            /* isPositionEditable = */ false,
            /* isSelectable = */ false,
            /* isAdd = */ false,
            /* isDrag = */ false,
            /* isRotate = */ false,
            /* polygonId = */ null
        )
    }

}

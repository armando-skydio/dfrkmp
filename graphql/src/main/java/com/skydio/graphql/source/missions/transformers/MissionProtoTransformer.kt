package com.skydio.graphql.source.missions.transformers

import com.google.protobuf.util.JsonFormat
import com.skydio.graphql.WaypointMissionQuery
import skills.MissionOuterClass.Mission

class MissionProtoTransformer(private val edge: WaypointMissionQuery.Edge) {
    operator fun invoke(): Mission? {
        edge.node?.currentSpec?.json?.also { json ->
            val mission = Mission.newBuilder()
            val cleaned = json.replace("\\\\", "")
            JsonFormat.parser().ignoringUnknownFields().merge(cleaned, mission)
            return mission.build()
        }
        return null
    }
}

class MissionsProtoTransformer(private val edges: List<WaypointMissionQuery.Edge>) {

    operator fun invoke(): MutableList<Mission> {
        val missions = mutableListOf<Mission>()
        edges.forEach { edge ->
            MissionProtoTransformer(edge).invoke()?.let { mission ->
                missions.add(mission)
            }
        }
        return missions
    }

    companion object {
        private const val rogueX = 2.13362594489149
        private const val rogueY = 0.0
    }

}

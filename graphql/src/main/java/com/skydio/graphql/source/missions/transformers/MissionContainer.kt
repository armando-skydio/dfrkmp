package com.skydio.graphql.source.missions.transformers

import android.util.SparseArray
import com.skydio.djinni.mvvm.MissionFileData
import com.skydio.graphql.WaypointMissionQuery
import com.skydio.util.SkyLog
import skills.MissionOuterClass
import java.io.File
import javax.inject.Inject

data class Missions internal constructor(internal val missions: List<MissionContainer>) {

    private val sparseIds = SparseArray<String>()

    init {
        missions.mapIndexed { index, missionContainer ->
            sparseIds.append(index, missionContainer.id)
        }
    }

    val missionFileDataList: List<MissionFileData>
        get() = missions.map { it.missionFileData }

    val missionList: List<MissionOuterClass.Mission>
        get() = missions.map { it.mission }

    fun getMissionById(id: String): MissionOuterClass.Mission? {
        val index = sparseIds.indexOfValue(id)
        val missions = missionList
        return if (index in 0..missions.lastIndex) {
            missions[index]
        } else {
            null
        }
    }

    fun getMissionFileDataById(id: String): MissionFileData? {
        val index = sparseIds.indexOfValue(id)
        val missions = missionFileDataList
        return if (index in 0..missions.lastIndex) {
            missions[index]
        } else {
            null
        }
    }

    companion object {
        val empty: Missions
            get() = Missions(emptyList())

    }

}

internal data class MissionContainer(
    val id: String, val missionFileData: MissionFileData, val mission: MissionOuterClass.Mission
)

internal class MissionsTransformer @Inject constructor() {
    operator fun invoke(edges: List<WaypointMissionQuery.Edge>): Missions {
        val missionContainers: List<MissionContainer> = edges.mapNotNull {
            val uid = it.node?.uuid ?: return@mapNotNull null
            val mission = MissionDataTransformer(it, uid).invoke() ?: return@mapNotNull null
            val missionProto = MissionProtoTransformer(it).invoke() ?: return@mapNotNull null
            MissionContainer(uid, mission, missionProto)
        }
        return Missions(missionContainers)
    }

}

fun MissionOuterClass.Mission.writeToFile(
    fileDir: String,
    fileName: String
): Boolean {
    val dir = File(fileDir)
    if (dir.exists().not()) {
        dir.mkdirs()
    } else if (dir.isDirectory.not()) {
        throw IllegalArgumentException("$fileDir must be a directory")
    }
    val outFile = File(dir, "${fileName}.mission")
    runCatching {
        this.toByteArray().inputStream().copyTo(outFile.outputStream())
        return true
    }.getOrElse { e ->
        SkyLog.e("MissionContainer", "Failed to write to file ${outFile.name}: ${e.message}")
    }
    return false
}

internal val WaypointMissionQuery.Data.nonNullEdges: List<WaypointMissionQuery.Edge>?
    get() = this.organization?.missionTemplates?.edges?.filterNotNull()

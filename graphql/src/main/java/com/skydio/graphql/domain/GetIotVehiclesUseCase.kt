package com.skydio.graphql.domain

import com.skydio.cloud.domain.GetUserInfoUseCase
import com.skydio.graphql.source.VehicleSource
import com.skydio.graphql.type.CommonFlightPhaseEnumEnum
import com.skydio.graphql.type.vehicletype
import com.skydio.platform.Result
import java.lang.Exception
import javax.inject.Inject

class GetIotVehiclesUseCase @Inject constructor(
    private val vehicleSource: VehicleSource,
    private val getUserInfoUseCase: GetUserInfoUseCase
) {
    suspend operator fun invoke(pageSize: Int = 100): Result<List<IotVehicle>> {
        val vehicleList = mutableListOf<IotVehicle>()
        val id = getUserInfoUseCase().organizationId ?: ""
        // NOTE(armando) - WE need cloud team to add support on GraphQL to only get teleop vehicles
        if (id.isEmpty()) {
            return Result {
                throw Exception("No Organization ID")
            }
        }
        return Result {
            vehicleSource.getVehicleList(
                orgId = id,
                pageSize = pageSize,
            ).filter { it.teleopEnabled }.forEach { node ->
                node.let {
                    // vehicleId & displayName are switched here to match cloud
                    vehicleList.add(
                        IotVehicle(
                            graphQlId = it.id,
                            displayName = it.displayName,
                            vehicleId = it.vehicleId,
                            skydioSerial = it.skydioSerial ?: "",
                            orgId = it.organization?.id.toString(),
                            orgName = it.organization?.name.toString(),
                            vehicleType = it.vehicleType,
                            isOnline = it.isOnline ?: false,
                            teleopEnabled = it.teleopEnabled,
                            skillKey = it.skillKey ?: "",
                            runmodeName = it.runModeName ?: "",
                            location = Location(
                                latitude = it.location?.latitude ?: 0.0,
                                longitude = it.location?.longitude ?: 0.0
                            ),
                            batteryStatus = BatteryStatus(
                                percentage = it.batteryStatus?.percentage ?: 0.0,
                                charging = it.batteryStatus?.charging ?: false
                            ),
                            flightPhase = getFlightPhase(it.flightPhase),
                            pilot = Pilot(
                                displayName = it.currentPilot?.displayName ?: "",
                                email = it.currentPilot?.email ?: ""
                            )
                        )
                    )
                }
            }
            vehicleList.toList()
        }
    }

    fun getFlightPhase(flightPhase: CommonFlightPhaseEnumEnum?): String {

        var returnedFlightPhase = "Unknown"
        flightPhase.let {
            returnedFlightPhase =
                when (it) {
                    CommonFlightPhaseEnumEnum.ARMED_IN_DOCK, CommonFlightPhaseEnumEnum.REST -> "Rest"
                    CommonFlightPhaseEnumEnum.FLIGHT_PROCESSES_CHECK, CommonFlightPhaseEnumEnum.HAND_TAKEOFF_PREP, CommonFlightPhaseEnumEnum.LOGGING_START,
                    CommonFlightPhaseEnumEnum.PREP, CommonFlightPhaseEnumEnum.PREP_CLEANUP, CommonFlightPhaseEnumEnum.READY_FOR_HAND_TAKEOFF,
                    CommonFlightPhaseEnumEnum.SAFETY_CHECK, CommonFlightPhaseEnumEnum.WAIT_FOR_ARM -> "Pre Flight"

                    CommonFlightPhaseEnumEnum.FLYING, CommonFlightPhaseEnumEnum.TEGRA_REBOOT_PREP -> "Flying"
                    CommonFlightPhaseEnumEnum.POST_FLIGHT -> "Post Flight"
                    else -> "Unknown"
                }
        }

        return returnedFlightPhase
    }

    // TODO(bojanin): these abstractions are stupid and i should use the djinni ones
    data class BatteryStatus(val percentage: Double, val charging: Boolean)

    // Graphql query doesnt return altitude, so omit it
    data class Location(val latitude: Double, val longitude: Double)
    data class Pilot(val displayName: String, val email: String)
    data class IotVehicle(
        val graphQlId: String,
        val displayName: String,
        val vehicleId: String,
        val skydioSerial: String,
        val orgId: String,
        val orgName: String,
        val vehicleType: vehicletype,
        val isOnline: Boolean,
        val teleopEnabled: Boolean,
        val skillKey: String,
        val runmodeName: String,
        val location: Location,
        val batteryStatus: BatteryStatus,
        val flightPhase: String,
        val pilot: Pilot
    )

    companion object {
        private const val TAG = "GetIotVehiclesUseCase"
    }
}

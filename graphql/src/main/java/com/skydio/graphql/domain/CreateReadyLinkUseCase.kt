package com.skydio.graphql.domain

import com.skydio.graphql.source.ReadyLinkSource
import com.skydio.platform.Result
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CreateReadyLinkUseCase @Inject constructor(
    private val readyLink: ReadyLinkSource,
    private val getIotVehicleByIdUseCase: GetIotVehicleByIdUseCase,
    private val getOrganizationNodeIdUseCase: GetOrganizationNodeIdUseCase,
) {
    suspend operator fun invoke(
        vehicleId: String,
        name: String,
    ): Result<String> {
        val orgUuid = when (val vehicle = getIotVehicleByIdUseCase(vehicleId)) {
            is Result.Success -> vehicle.data?.organizationId ?: ""
            is Result.Failure -> ""
        }
        val organizationNodeId = when (val orgNodeId = getOrganizationNodeIdUseCase(orgUuid)) {
            is Result.Success -> orgNodeId.data.id
            is Result.Failure -> ""
        }

        val vehicleUuid = when (val vehicle = getIotVehicleByIdUseCase(vehicleId)) {
            is Result.Success -> vehicle.data?.vehicleUuid ?: ""
            is Result.Failure -> ""
        }

        val currentDateTime = LocalDateTime.now()
        val startsAt = DateTimeFormatter.ISO_INSTANT.format(currentDateTime.toInstant(ZoneOffset.UTC))
        // Note(kristy): ReadyLink should expire 24 hours later
        val expiresAt = DateTimeFormatter.ISO_INSTANT.format(currentDateTime.plusHours(24).toInstant(ZoneOffset.UTC))

        return Result {
            readyLink.createReadyLink(
                organizationNodeId,
                vehicleUuid,
                expiresAt,
                name,
                startsAt,
            )?.let {
                it.createSharedLink?.sharedLink?.token?.let {
                    "$DEFAULT_SHARED_LINK$it"
                }
            }?: ""
        }
    }

    companion object {
        private const val DEFAULT_SHARED_LINK = "https://cloud.skydio.com/shared_link/"
    }
}

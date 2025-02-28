package com.skydio.graphql.domain

import com.skydio.graphql.source.ControllerSource
import com.skydio.platform.Result
import javax.inject.Inject

class GetControllerBySerialUseCase @Inject constructor(
    private val controllerSource: ControllerSource
) {
    suspend operator fun invoke(serial: String) =
        Result {
            controllerSource.getController(serial)
        }
}
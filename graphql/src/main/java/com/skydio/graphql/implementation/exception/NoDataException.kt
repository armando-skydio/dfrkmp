package com.skydio.graphql.implementation.exception

class NoDataException(message: String = "No data received from the source") : RuntimeException(message)

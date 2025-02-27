package com.skydio.graphql.implementation.exception

class InvalidResultException(message: String = "Invalid data received from the source") : RuntimeException(message)

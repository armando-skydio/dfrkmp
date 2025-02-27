package com.skydio.graphql.implementation.exception

class QueryException(
    message: String? = "Apollo query failed to get response",
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    internal constructor(
        throwable: Throwable
    ) : this(throwable.message, throwable)
}

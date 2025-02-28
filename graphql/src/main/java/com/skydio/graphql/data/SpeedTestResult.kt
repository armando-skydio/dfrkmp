package com.skydio.graphql.data

data class SpeedTestResult (
    val intervals: List<Interval>? = null
)

data class Interval (
    val streams: List<Stream>?,
    val sum: Sum?
)

data class Stream (
    val seconds: Float,
    val bytes: Long,
    val bits_per_second: Float
)

data class Sum (
    val bytes: Long,
    val bits_per_second: Float
)

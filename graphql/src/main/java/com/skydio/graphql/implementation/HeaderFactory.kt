package com.skydio.graphql.implementation

interface HeaderFactory {
    fun getAuthHeaders(): List<Pair<String, String>>
    fun addAuthHeader(key: String, value: String)
    fun cleanAuthHeaders()
}

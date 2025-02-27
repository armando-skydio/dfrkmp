package com.skydio.graphql.implementation

import javax.inject.Inject

class AuthorizationHeaderFactory @Inject constructor() : HeaderFactory {

    private val headerList: MutableList<Pair<String, String>> = mutableListOf()

    override fun getAuthHeaders(): List<Pair<String, String>> = headerList

    override fun addAuthHeader(key: String, value: String) {
        headerList.add(key to value)
    }

    override fun cleanAuthHeaders() = headerList.clear()
}

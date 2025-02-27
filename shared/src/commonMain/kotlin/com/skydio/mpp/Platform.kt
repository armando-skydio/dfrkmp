package com.skydio.mpp

import com.linecorp.abc.location.ABCLocation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

fun extra() {

}
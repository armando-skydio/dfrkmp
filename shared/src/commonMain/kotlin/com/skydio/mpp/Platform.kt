package com.skydio.mpp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

fun extra() {

}
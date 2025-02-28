package com.skydio.mpp.utils

import kotlinx.coroutines.CoroutineDispatcher


internal interface AppDispatcher {
    val dispatcher : CoroutineDispatcher
}
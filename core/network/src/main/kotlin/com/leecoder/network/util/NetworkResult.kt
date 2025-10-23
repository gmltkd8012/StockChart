package com.leecoder.network.util

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Failed(val code: Int?, val message: String?) : NetworkResult<Nothing>()
}
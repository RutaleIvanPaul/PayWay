package com.payway.paywaytransactions.data.dashboard.model

/**
 * Wrapper class to track state and hold data of API responses
 */
sealed class MyResult<out T> {
    data class Success<out T>(val data: T) : MyResult<T>()
    data class Error(val exception: Exception) : MyResult<Nothing>()
}

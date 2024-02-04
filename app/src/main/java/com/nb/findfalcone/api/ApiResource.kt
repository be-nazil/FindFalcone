package com.nb.findfalcone.api

import androidx.annotation.Keep

@Keep
data class ApiResource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val httpCode: Int,
    var isLoaded: Boolean
) {
    companion object {

        fun <T> success(data: T?, httpCode: Int): ApiResource<T> {
            return ApiResource(Status.SUCCESS, data, null, httpCode, true)
        }

        fun <T> error(msg: String, data: T?, httpCode: Int): ApiResource<T> {
            return ApiResource(Status.ERROR, data, msg, httpCode, false)
        }

        fun <T> loading(data: T?): ApiResource<T> {
            return ApiResource(Status.LOADING, data, null, 0, false)
        }

        fun <T> failure(msg: String? = null): ApiResource<T> {
            return ApiResource(Status.ERROR, null, msg, 0, false)
        }

    }
}

@Keep
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
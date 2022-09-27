package com.eldebvs.consulting.domain.model

sealed class Response<out T> {
    object Loading: Response<Nothing>()
    data class Success<out T>(
        val data : T
    ): Response<T>()
    data class Failure(
        val e: java.lang.Exception
    ): Response<Nothing>()
}
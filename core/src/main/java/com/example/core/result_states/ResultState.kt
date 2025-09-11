package com.example.core.result_states

import retrofit2.HttpException
import java.io.IOException

sealed class ResultState<out T> {
    data object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val message: String, val cause: Throwable? = null) : ResultState<Nothing>()
}

fun Throwable.userMessage(): String = when (this) {
    is IOException -> "Network error. Check your connection."
    is HttpException -> "Server error (${code()})."
    else -> "Something went wrong."
}
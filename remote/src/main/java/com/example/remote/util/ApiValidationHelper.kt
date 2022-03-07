package com.example.remote.util

import com.example.remote.Constants
import retrofit2.Response


suspend fun <T> apiValidator(call: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            response.body()?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(data = null, Constants.UNKNOWN_ERROR)
        } else {
            Resource.Error(data = null, response.message())
        }
    } catch (e: Exception) {
        Resource.Error(data = null, Constants.SERVER_ERROR)
    }
}


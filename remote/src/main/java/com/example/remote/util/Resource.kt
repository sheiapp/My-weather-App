package com.example.remote.util


/**
 * Created by Shaheer cs on 19/02/2022.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Loading<T>(data: T? = null) : Resource<T>(data = data)
    class Error<T>(data: T? = null, message: String? = null) :
        Resource<T>(data = data, message = message)
}


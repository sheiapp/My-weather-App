package com.example.myweatherapp

import com.example.remote.util.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Created by Shaheer cs on 12/03/2022.
 */

suspend inline fun <RequestType, ResultType> networkBoundResource(
    crossinline query: () -> ResultType,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query()
    if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        saveFetchResult(fetch())
        emit(Resource.Success(query()))
    } else {
        emit(Resource.Success(query()))
    }
}.catch { e ->
    emit(Resource.Error(query(), e.message))
}



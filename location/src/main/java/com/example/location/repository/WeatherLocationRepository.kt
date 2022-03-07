package com.example.location.repository

import android.annotation.SuppressLint
import android.content.Context
import com.example.location.utils.cacheTheLocation
import com.example.location.utils.getCachedLocation
import com.google.android.gms.location.FusedLocationProviderClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Shaheer cs on 05/03/2022.
 */
class WeatherLocationRepository @Inject constructor(
    private val context: Context,
    private val mFusedLocationClient: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission", "SetTextI18n")
    suspend fun getLocation(): String? = suspendCoroutine { continuation ->
        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.exception is Exception || task.result == null) {
                continuation.resume(context.getCachedLocation())
            } else {
                val latLong = "${task.result.latitude},${task.result.longitude}"
                context.cacheTheLocation(latLong)
                continuation.resume(latLong)
            }
        }
    }
}
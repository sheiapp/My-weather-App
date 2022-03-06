package com.example.location.utils



/**
 * Created by Shaheer cs on 05/03/2022.
 */
sealed class LocationResource<T>(
    val data: T? = null,
    var isLocationEnabled: Boolean = true,
    var IsPermissionGranted: Boolean = true
) {
    class Success<T>(data: T) : LocationResource<T>(data = data)
    class LocationStatus<T>(data: T? = null, isLocationEnabled: Boolean) : LocationResource<T>(data = data, isLocationEnabled = isLocationEnabled)
    class PermissionStatus<T>(data: T? = null, IsPermissionGranted: Boolean) : LocationResource<T>(data = data, IsPermissionGranted = IsPermissionGranted)
}
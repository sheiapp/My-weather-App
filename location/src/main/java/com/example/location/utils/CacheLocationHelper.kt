package com.example.location.utils

import android.content.Context
import com.example.location.utils.Constants.CACHE_LOCATION_ID
import com.example.location.utils.Constants.DEFAULT_LAT_LONG
import com.example.location.utils.Constants.LOCATION_KEY

/**
 * Created by Shaheer cs on 06/03/2022.
 */

fun Context.cacheTheLocation(latLong: String) {
    this.getSharedPreferences(CACHE_LOCATION_ID, Context.MODE_PRIVATE).edit().apply {
        putString(LOCATION_KEY, latLong)
        apply()
    }
}

fun Context.getCachedLocation() =
    this.getSharedPreferences(CACHE_LOCATION_ID, Context.MODE_PRIVATE)
        .getString(LOCATION_KEY, DEFAULT_LAT_LONG)


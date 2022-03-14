package com.example.local.utils

import androidx.room.TypeConverter
import com.example.local.model.Forecast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Shaheer cs on 12/03/2022.
 */
object ForecastTypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String): List<Forecast> {
        val listType = object : TypeToken<List<Forecast>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromList(list: List<Forecast>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
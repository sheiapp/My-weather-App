package com.example.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.local.utils.ForecastTypeConverter

/**
 * Created by Shaheer cs on 05/03/2022.
 */

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
@TypeConverters(ForecastTypeConverter::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDAO

    companion object {
        const val DATABASE_NAME="weather_database"
    }
}
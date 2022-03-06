package com.example.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by Shaheer cs on 05/03/2022.
 */

@Database(entities = [FavoriteCityWeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDataBase : RoomDatabase(){
    abstract fun getWeatherDao(): WeatherDAO
}
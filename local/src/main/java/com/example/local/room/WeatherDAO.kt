package com.example.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Shaheer cs on 05/03/2022.
 */
@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun persistFavoriteCityData(favoriteCityWeatherEntity: FavoriteCityWeatherEntity)

    @Query("SELECT * FROM favourite_city_weather_list_table")
    suspend fun getPersistedListOfFavoriteCities(): List<FavoriteCityWeatherEntity>
}
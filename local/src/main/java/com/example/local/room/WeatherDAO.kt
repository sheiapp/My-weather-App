package com.example.local.room

import androidx.room.*

/**
 * Created by Shaheer cs on 05/03/2022.
 */
@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun persistWeatherAndForecastData(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather_and_forecast_table WHERE is_favorite_city = 0")
    fun getPersistedWeatherAndForecastData(): WeatherEntity

    @Query("SELECT * FROM weather_and_forecast_table WHERE is_favorite_city = 1")
    fun getPersistedListOfFavoriteCities(): List<WeatherEntity>

    @Query("DELETE  FROM  weather_and_forecast_table WHERE is_favorite_city = 0")
    suspend fun deleteAllPersistedWeatherData()

    @Query("UPDATE weather_and_forecast_table SET is_temp_in_celsius = :isCelsius")
    suspend fun updateTempUnit(isCelsius: Boolean)

    @Query("UPDATE weather_and_forecast_table SET is_favorite_city =1 WHERE city_name=:cityName")
    suspend fun markCurrentCityAsFavorite(cityName: String)
}
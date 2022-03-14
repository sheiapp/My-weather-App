package com.example.local.room

import androidx.room.*

/**
 * Created by Shaheer cs on 05/03/2022.
 */
@Dao
interface WeatherDAO {
    @Transaction
    suspend fun addCurrentCityAsFavorite(cityName: String) {
        if (!isCityAlreadyMarkedAsFavorite(cityName)) {
            markCurrentCityAsFavorite(cityName)
        }
    }

    @Transaction
    suspend fun persistFetchedWeatherAndForecastData(weatherEntity: WeatherEntity) {
        deleteAllPersistedWeatherData()
        persistWeatherAndForecastData(weatherEntity)
    }

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

    @Query("SELECT EXISTS (SELECT * FROM weather_and_forecast_table WHERE city_name = :cityName and is_favorite_city=1 )")
    fun isCityAlreadyMarkedAsFavorite(cityName: String): Boolean
}
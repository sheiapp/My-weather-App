package com.example.myweatherapp.repository

import com.example.local.room.WeatherEntity
import com.example.remote.model.WeatherAndForecastResponseData

/**
 * Created by Shaheer cs on 14/03/2022.
 */
interface WeatherRepository {

    suspend fun getLocation(): String?

    suspend fun fetchWeatherAndForecast(query: String?): WeatherAndForecastResponseData

    suspend fun persistFetchedWeatherAndForecastData(weatherEntity: WeatherEntity)

    suspend fun addCurrentCityAsFavorite(cityName: String)

    suspend fun updateTempUnit(isCelsius: Boolean)

    fun getPersistedWeatherAndForecastData(): WeatherEntity?

    fun getPersistedListOfFavoriteCities(): List<WeatherEntity>
}
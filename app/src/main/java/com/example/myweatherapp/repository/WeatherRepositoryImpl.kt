package com.example.myweatherapp.repository

import androidx.room.withTransaction
import com.example.local.room.WeatherDataBase
import com.example.local.room.WeatherEntity
import com.example.location.WeatherLocationProvider
import com.example.remote.network.WeatherApi
import javax.inject.Inject

/**
 * Created by Shaheer cs on 12/03/2022.
 */
class WeatherRepositoryImpl @Inject constructor(
    private val _weatherApi: WeatherApi,
    private val _weatherDatabase: WeatherDataBase,
    private val _weatherLocationProvider: WeatherLocationProvider
) : WeatherRepository {
    override suspend fun getLocation() =
        _weatherLocationProvider.getLocation()

    override suspend fun fetchWeatherAndForecast(query: String?) =
        _weatherApi.getWeatherAndForecast(query = query)

    override fun getPersistedWeatherAndForecastData() =
        _weatherDatabase.getWeatherDao().getPersistedWeatherAndForecastData()

    override suspend fun persistFetchedWeatherAndForecastData(weatherEntity: WeatherEntity) =
        _weatherDatabase.getWeatherDao().persistFetchedWeatherAndForecastData(weatherEntity)

    override suspend fun addCurrentCityAsFavorite(cityName: String) =
        _weatherDatabase.getWeatherDao().addCurrentCityAsFavorite(cityName)


    override suspend fun updateTempUnit(isCelsius: Boolean) {
        _weatherDatabase.getWeatherDao().updateTempUnit(isCelsius)
    }

    override fun getPersistedListOfFavoriteCities() =
        _weatherDatabase.getWeatherDao().getPersistedListOfFavoriteCities()

}
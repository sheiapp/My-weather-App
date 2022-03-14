package com.example.myweatherapp.repository

import android.util.Log
import androidx.room.withTransaction
import com.example.local.model.Forecast
import com.example.local.room.WeatherDataBase
import com.example.local.room.WeatherEntity
import com.example.location.WeatherLocationProvider
import com.example.myweatherapp.networkBoundResource
import com.example.remote.network.WeatherApi
import com.example.remote.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Shaheer cs on 12/03/2022.
 */
class WeatherRepository @Inject constructor(
    private val _weatherApi: WeatherApi,
    private val _weatherDatabase: WeatherDataBase,
    private val _weatherLocationProvider: WeatherLocationProvider
) {

    suspend fun getWeatherAndForecastBasedOnLocation(): Flow<Resource<WeatherEntity>> {
        val location = _weatherLocationProvider.getLocation()
        Log.d(this.javaClass.simpleName, location.toString())
        return getWeatherAndForecast(location, shouldFetch = true)
    }

    suspend fun getWeatherAndForecast(
        query: String?,
        shouldFetch: Boolean = false
    ) = networkBoundResource(query = {
        _weatherDatabase.getWeatherDao().getPersistedWeatherAndForecastData()
    }, fetch = {
        _weatherApi.getWeatherAndForecast(query = query).let {
            WeatherEntity(
                cityName = it.location.region,
                conditionIcon = it.current.condition.icon,
                conditionText = it.current.condition.text,
                precipitation = it.current.precipIn.toString(),
                windSpeed = it.current.windKph.toString(),
                humidity = it.current.humidity.toString(),
                visibility = it.current.visKm.toString(),
                tempInCelsius = it.current.tempC.toString(),
                tempInFahrenheit = it.current.tempF.toString(),
                forecast = it.forecast.forecastday.map { forecast ->
                    Forecast(
                        conditionText = forecast.day.condition.text,
                        conditionIcon = forecast.day.condition.icon,
                        date = forecast.date
                    )
                }
            )
        }
    },

        saveFetchResult = { result ->
            _weatherDatabase.withTransaction {
                result.let {
                    _weatherDatabase.getWeatherDao().deleteAllPersistedWeatherData()
                    it.let { it1 ->
                        _weatherDatabase.getWeatherDao().persistWeatherAndForecastData(it1)
                    }
                }
            }
        }, shouldFetch = { shouldFetch }
    )

    suspend fun setTheCurrentSelectedCityAsFavorite(cityName: String) {
        _weatherDatabase.getWeatherDao().addCurrentCityAsFavorite(cityName)
    }

    suspend fun updateTempUnit(isCelsius: Boolean) {
        _weatherDatabase.getWeatherDao().updateTempUnit(isCelsius)
    }

    fun getPersistedListOfFavoriteCities() =
        _weatherDatabase.getWeatherDao().getPersistedListOfFavoriteCities()

}
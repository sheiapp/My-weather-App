package com.example.myweatherapp.usecase

import com.example.local.model.Forecast
import com.example.local.room.WeatherEntity
import com.example.myweatherapp.networkBoundResource
import com.example.myweatherapp.repository.WeatherRepository
import com.example.remote.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Shaheer cs on 14/03/2022.
 */
class WeatherAndForecastUseCase(private val weatherRepository: WeatherRepository) {

    suspend fun getWeatherAndForecastBasedOnLocation(): Flow<Resource<WeatherEntity?>> {
        val location = weatherRepository.getLocation()
        return getWeatherAndForecast(location, shouldFetch = true)
    }

    suspend fun getWeatherAndForecast(
        query: String?,
        shouldFetch: Boolean = false
    ) = networkBoundResource(query = {
        weatherRepository.getPersistedWeatherAndForecastData()
    }, fetch = {
        weatherRepository.fetchWeatherAndForecast(query = query).let {
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
                        conditionText = forecast.day?.condition?.text,
                        conditionIcon = forecast.day?.condition?.icon,
                        date = forecast.date
                    )
                }
            )
        }
    }, saveFetchResult = { result ->
        weatherRepository.persistFetchedWeatherAndForecastData(result)
    }, shouldFetch = { shouldFetch }
    )

    suspend fun setTheCurrentSelectedCityAsFavorite(cityName: String) {
        weatherRepository.addCurrentCityAsFavorite(cityName)
    }

    suspend fun updateTempUnit(isCelsius: Boolean) {
        weatherRepository.updateTempUnit(isCelsius)
    }
}
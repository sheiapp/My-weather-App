package com.example.remote.usecase

import com.example.location.repository.WeatherLocationRepository
import com.example.remote.model.WeatherAndForecastResponseData
import com.example.remote.repository.WeatherDataRepository
import com.example.remote.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


import javax.inject.Inject

/**
 * Created by Shaheer cs on 06/03/2022.
 */
class WeatherAndForecastBasedOnCityNameAndLocationUseCase @Inject constructor(
    private val _weatherDataRepository: WeatherDataRepository,
    private val _weatherLocationRepository: WeatherLocationRepository
) {
    suspend fun getWeatherAndForecastBasedOnCityName(cityName: String): Flow<Resource<WeatherAndForecastResponseData>> {
        return flow { emit(_weatherDataRepository.getWeatherAndForecast(cityName)) }
    }

    suspend fun getWeatherAndForecastBasedOnLocation(): Flow<Resource<WeatherAndForecastResponseData>> {
        val location = _weatherLocationRepository.getLocation()
        return flow { emit(_weatherDataRepository.getWeatherAndForecast(location)) }
    }

}
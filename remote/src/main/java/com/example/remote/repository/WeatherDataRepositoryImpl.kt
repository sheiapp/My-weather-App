package com.example.remote.repository

import com.example.remote.network.WeatherApi
import com.example.remote.util.apiValidator
import javax.inject.Inject


/**
 * Created by Shaheer cs on 04/03/2022.
 */
class WeatherDataRepositoryImpl @Inject constructor(private val _weatherApi: WeatherApi) :
    WeatherDataRepository {

    override suspend fun getWeatherAndForecastBasedOnLocation(query: String) = apiValidator {
        _weatherApi.getWeatherAndForecastBasedOnLocation(query =query )
    }

}
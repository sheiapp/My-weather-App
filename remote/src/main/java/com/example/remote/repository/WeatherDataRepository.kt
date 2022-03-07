package com.example.remote.repository

import com.example.remote.model.WeatherAndForecastResponseData
import com.example.remote.util.Resource

/**
 * Created by Shaheer cs on 04/03/2022.
 */
interface WeatherDataRepository {

    suspend fun getWeatherAndForecast(query: String?): Resource<WeatherAndForecastResponseData>
}
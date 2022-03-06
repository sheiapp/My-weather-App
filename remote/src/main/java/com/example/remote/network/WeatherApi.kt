package com.example.remote.network

import com.example.remote.Constants.NO_OF_FORECAST_DAYS
import com.example.remote.model.WeatherAndForecastResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Shaheer cs on 04/03/2022.
 */
interface WeatherApi {
    @GET("v1/forecast.json?key=e27351089ab9455ca1072601220403")
    suspend fun getWeatherAndForecastBasedOnLocation(
        @Query("q") query: String,
        @Query("days") noOfDays: String = NO_OF_FORECAST_DAYS
    ): Response<WeatherAndForecastResponseData>


}
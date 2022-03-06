package com.example.remote.model


import com.google.gson.annotations.SerializedName

data class WeatherAndForecastResponseData(
    @SerializedName("current")
    val current: Current,
    @SerializedName("forecast")
    val forecast: Forecast,
    @SerializedName("location")
    val location: Location
)
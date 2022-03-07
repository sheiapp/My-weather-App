package com.example.remote.model


import com.google.gson.annotations.SerializedName

data class WeatherAndForecastResponseData(
    @SerializedName("current")
    var current: Current? = null,
    @SerializedName("forecast")
    val forecast: Forecast? = null,
    @SerializedName("location")
    val location: Location? = null
)
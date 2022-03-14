package com.example.remote.model


import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday")
    var forecastday: List<Forecastday>
)
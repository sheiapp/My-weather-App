package com.example.local.room

import androidx.room.*
import com.example.local.model.Forecast

/**
 * Created by Shaheer cs on 05/03/2022.
 */
@Entity(
    tableName = "weather_and_forecast_table"
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var columnId: Int = 0,
    @ColumnInfo(name = "city_name")
    val cityName: String,
    @ColumnInfo(name = "condition_icon")
    val conditionIcon: String?,
    @ColumnInfo(name = "condition")
    val conditionText: String?,
    @ColumnInfo(name = "precipitation")
    val precipitation: String,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: String,
    @ColumnInfo(name = "humidity")
    val humidity: String,
    @ColumnInfo(name = "visibility")
    val visibility: String,
    @ColumnInfo(name = "temperature_in_celsius")
    val tempInCelsius: String,
    @ColumnInfo(name = "temperature_in_fahrenheit")
    val tempInFahrenheit: String,
    @ColumnInfo(name = "forecast")
    val forecast: List<Forecast>,
    @ColumnInfo(name = "is_favorite_city")
    var isFavoriteCity: Boolean = false,
    @ColumnInfo(name = "is_temp_in_celsius")
    var isTempInCelsius: Boolean = true
)

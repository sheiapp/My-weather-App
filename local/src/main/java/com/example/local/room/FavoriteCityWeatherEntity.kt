package com.example.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Shaheer cs on 05/03/2022.
 */
@Entity(
    tableName = "favourite_city_weather_list_table",
    indices = [Index(value = ["city_name"], unique = true)]
)
data class FavoriteCityWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var columnId: Int = 0,
    @ColumnInfo(name = "city_name")
    val cityName: String?,
    @ColumnInfo(name = "condition_icon")
    val conditionIcon: String?,
    @ColumnInfo(name = "condition")
    val conditionText: String?,
    @ColumnInfo(name = "temperatureInCelsius")
    val tempInCelsius: String?,
    @ColumnInfo(name = "temperatureInFahrenheit")
    val tempInFahrenheit: String?
)

package com.example.myweatherapp.repository

import com.example.local.room.WeatherEntity
import com.example.location.utils.Constants.DEFAULT_LAT_LONG
import com.example.remote.model.*

/**
 * Created by Shaheer cs on 14/03/2022.
 */
class FakeWeatherRepositoryImpl : WeatherRepository {

    private val _weatherAndForecastData = mutableListOf<WeatherEntity>()
    private val _location = DEFAULT_LAT_LONG
    var weatherAndForecastResponseData: WeatherAndForecastResponseData? = null
    private val _forecastDays = mutableListOf<Forecastday>()

    init {
        ('a'..'g').forEachIndexed { index, c ->
            _forecastDays.add(
                Forecastday(
                    day = Day(
                        condition = Condition(
                            icon = c.toString(),
                            text = c.toString()
                        )
                    )
                )
            )
        }
        weatherAndForecastResponseData = WeatherAndForecastResponseData(
            current = Current(
                condition = Condition(
                    icon = "http://www.google.com",
                    text = "cloudy"
                ),
                precipIn = 0.0,
                windKph = 15.1,
                humidity = 70,
                visKm = 10.0,
                tempC = 27.0,
                tempF = 80.6
            ),
            forecast = Forecast(forecastday = _forecastDays),
            location = Location(region = "dubai")
        )
    }

    override suspend fun getLocation() = _location

    override suspend fun fetchWeatherAndForecast(query: String?): WeatherAndForecastResponseData {
        return weatherAndForecastResponseData!!
    }

    override suspend fun persistFetchedWeatherAndForecastData(weatherEntity: WeatherEntity) {
        _weatherAndForecastData.add(weatherEntity)
    }


    override suspend fun addCurrentCityAsFavorite(cityName: String) {
        _weatherAndForecastData.find {
            it.cityName == cityName
        }?.isFavoriteCity = true
    }

    override suspend fun updateTempUnit(isCelsius: Boolean) {
        _weatherAndForecastData.map {
            it.isTempInCelsius = isCelsius
        }
    }

    override fun getPersistedWeatherAndForecastData(): WeatherEntity? {
        return _weatherAndForecastData.find {
            !it.isFavoriteCity
        }
    }

    override fun getPersistedListOfFavoriteCities(): List<WeatherEntity> {
        return _weatherAndForecastData.filter {
            it.isFavoriteCity
        }
    }

}
package com.example.myweatherapp.usecase

import com.example.myweatherapp.repository.WeatherRepository
import com.example.myweatherapp.repository.FakeWeatherRepositoryImpl
import com.example.remote.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by Shaheer cs on 14/03/2022.
 */
class WeatherAndForecastUseCaseTest {

    private lateinit var weatherAndForecastUseCaseTest: WeatherAndForecastUseCase
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepositoryImpl()
        weatherAndForecastUseCaseTest = WeatherAndForecastUseCase(weatherRepository)


    }

    @Test
    fun `validate we can fetch the data using location as query param with getWeatherAndForecastBasedOnLocation`() = runBlocking {
        val weatherAndForecastData = weatherAndForecastUseCaseTest.getWeatherAndForecastBasedOnLocation().last()
        assertThat(weatherAndForecastData).isInstanceOf(Resource.Success::class.java)
        //isFavoriteCity always false for fetched data
        assertThat(weatherAndForecastData.data?.isFavoriteCity).isFalse()
    }

    @Test
    fun `validate we can fetch the data using city name as query param with getWeatherAndForecast`() = runBlocking {
        val cityName="dubai"
        val weatherAndForecastData = weatherAndForecastUseCaseTest.getWeatherAndForecast(cityName,true).last()
        assertThat(weatherAndForecastData).isInstanceOf(Resource.Success::class.java)
        //isFavoriteCity always false for fetched data
        assertThat(weatherAndForecastData.data?.isFavoriteCity).isFalse()
    }

    @Test
    fun `validate we are getting null when the response is null with getWeatherAndForecast`() =
        runBlocking {
            (weatherRepository as FakeWeatherRepositoryImpl).weatherAndForecastResponseData = null
            val cityName="dubai"
            val weatherAndForecastData = weatherAndForecastUseCaseTest.getWeatherAndForecast(cityName, true).last()
            assertThat(weatherAndForecastData).isInstanceOf(Resource.Error::class.java)
            assertThat(weatherAndForecastData.data).isNull()
        }

    @Test
    fun `validate that we can set the favorite city with setTheCurrentSelectedCityAsFavorite`()=
        runBlocking {
            weatherAndForecastUseCaseTest.getWeatherAndForecastBasedOnLocation().last()
            val cityName="dubai"
            weatherAndForecastUseCaseTest.setTheCurrentSelectedCityAsFavorite(cityName)
            val weatherDataList=weatherRepository.getPersistedListOfFavoriteCities()
            for (i in weatherDataList.indices)
            assertThat(weatherRepository.getPersistedListOfFavoriteCities()[i].isFavoriteCity).isTrue()
        }

    @Test
    fun `validate that we can update the temp unit with updateTempUnit`()= runBlocking {
        weatherAndForecastUseCaseTest.getWeatherAndForecastBasedOnLocation().last()
        weatherAndForecastUseCaseTest.updateTempUnit(true)
        assertThat(weatherRepository.getPersistedWeatherAndForecastData()?.isTempInCelsius).isTrue()
        weatherAndForecastUseCaseTest.updateTempUnit(false)
        assertThat(weatherRepository.getPersistedWeatherAndForecastData()?.isTempInCelsius).isFalse()
    }

}
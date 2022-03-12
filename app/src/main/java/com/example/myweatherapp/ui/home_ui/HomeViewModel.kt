package com.example.myweatherapp.ui.home_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.local.room.WeatherEntity
import com.example.location.WeatherLocationProvider
import com.example.myweatherapp.repository.WeatherRepository
import com.example.myweatherapp.worker.DailyWorker
import com.example.myweatherapp.worker.timeDiff
import com.example.remote.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Shaheer cs on 04/03/2022.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _workManager: WorkManager,
    private val _workConstraints: Constraints,
    private val _weatherRepository: WeatherRepository
) : ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()
    private val _weatherAndForecastDataSet = MutableStateFlow<Resource<WeatherEntity>?>(null)
    val weatherAndForecastDataSet = _weatherAndForecastDataSet.asStateFlow()
    private val _tempUnitState = MutableSharedFlow<Boolean?>()
    val tempUnitState = _tempUnitState.asSharedFlow()


    private val _workRequest = OneTimeWorkRequestBuilder<DailyWorker>().apply {
        setInitialDelay(timeDiff(), TimeUnit.MILLISECONDS)
        setConstraints(_workConstraints)
    }.build()


    init {
        scheduleWorkForNotifyingUserWithCurrentWeather()
        getWeatherAndForecastForLocation()
    }

    fun getWeatherAndForecastForCityName(cityName: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = _weatherRepository.getWeatherAndForecast(cityName, shouldFetch = true)
        response.collect {
            _weatherAndForecastDataSet.value = it
        }
    }


    fun getWeatherAndForecastForLocation() = viewModelScope.launch(Dispatchers.IO) {

        _weatherRepository.getWeatherAndForecastBasedOnLocation().collect {
            _tempUnitState.emit(it.data?.isTempInCelsius)
            _weatherAndForecastDataSet.value = it
        }
    }

    fun setTheCurrentSelectedCityAsFavorite() = viewModelScope.launch(Dispatchers.IO) {
        _weatherAndForecastDataSet.value?.data?.copy(isFavoriteCity = true)?.let {
            _weatherAndForecastDataSet.value = Resource.Success(it)
        }
        _weatherAndForecastDataSet.value?.data?.let {
            _weatherRepository.setTheCurrentSelectedCityAsFavorite(it.cityName)
        }
    }

    fun setTempUnit(isCelsius: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _weatherAndForecastDataSet.value?.data?.copy(isTempInCelsius = isCelsius)?.let {
            _weatherAndForecastDataSet.value = Resource.Success(it)
        }
        _weatherRepository.updateTempUnit(isCelsius)
    }

    private fun scheduleWorkForNotifyingUserWithCurrentWeather() {
        _workManager.enqueue(_workRequest)
    }

}
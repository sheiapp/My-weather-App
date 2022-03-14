package com.example.myweatherapp.ui.home_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.WorkManager
import com.example.local.room.WeatherEntity
import com.example.myweatherapp.usecase.WeatherAndForecastUseCase
import com.example.myweatherapp.worker.DailyWorker.Companion.scheduleWorkForNotifyingUserWithCurrentWeather
import com.example.remote.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shaheer cs on 04/03/2022.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    _workManager: WorkManager,
    _workConstraints: Constraints,
    private val _useCase: WeatherAndForecastUseCase
) : ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()
    private val _weatherAndForecastDataSet = MutableStateFlow<Resource<WeatherEntity?>?>(null)
    val weatherAndForecastDataSet = _weatherAndForecastDataSet.asStateFlow()
    private val _tempUnitState = MutableSharedFlow<Boolean?>()
    val tempUnitState = _tempUnitState.asSharedFlow()

    init {
        scheduleWorkForNotifyingUserWithCurrentWeather(_workManager, _workConstraints)
        getWeatherAndForecastForLocation()
    }

    fun getWeatherAndForecastForCityName(cityName: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = _useCase.getWeatherAndForecast(cityName, shouldFetch = true)
        response.collect {
            _weatherAndForecastDataSet.value = it
        }
    }


    fun getWeatherAndForecastForLocation() = viewModelScope.launch(Dispatchers.IO) {

        _useCase.getWeatherAndForecastBasedOnLocation().collect {
            if (it is Resource.Loading)
                _tempUnitState.emit(it.data?.isTempInCelsius)
            _weatherAndForecastDataSet.value = it
        }
    }

    fun setTheCurrentSelectedCityAsFavorite() = viewModelScope.launch(Dispatchers.IO) {
        _weatherAndForecastDataSet.value?.data?.copy(isFavoriteCity = true)?.let {
            _weatherAndForecastDataSet.value = Resource.Success(it)
        }
        _weatherAndForecastDataSet.value?.data?.let {
            _useCase.setTheCurrentSelectedCityAsFavorite(it.cityName)
        }
    }

    fun setTempUnit(isCelsius: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _weatherAndForecastDataSet.value?.data?.copy(isTempInCelsius = isCelsius)?.let {
            _weatherAndForecastDataSet.value = Resource.Success(it)
        }
        _useCase.updateTempUnit(isCelsius)
    }
}
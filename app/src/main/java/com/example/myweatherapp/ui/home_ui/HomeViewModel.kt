package com.example.myweatherapp.ui.home_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.local.repository.LocalWeatherRepository
import com.example.local.room.FavoriteCityWeatherEntity
import com.example.myweatherapp.worker.DailyWorker
import com.example.myweatherapp.worker.timeDiff
import com.example.remote.model.WeatherAndForecastResponseData
import com.example.remote.usecase.WeatherAndForecastBasedOnCityNameAndLocationUseCase
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
    private val _weatherAndForecastBasedOnCityNameAndLocationUseCase: WeatherAndForecastBasedOnCityNameAndLocationUseCase,
    private val _localWeatherRepository: LocalWeatherRepository,
    private val _workManager: WorkManager,
    private val _workConstraints: Constraints
) : ViewModel() {

    private val _weatherAndForecastData = MutableStateFlow(WeatherAndForecastResponseData())
    val weatherAndForecastData = _weatherAndForecastData.asStateFlow()
    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading = _isLoading.asSharedFlow()
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    private val _workRequest = OneTimeWorkRequestBuilder<DailyWorker>().apply {
        setInitialDelay(timeDiff(), TimeUnit.MILLISECONDS)
        setConstraints(_workConstraints)
    }.build()


    init {
        scheduleWorkForNotifyingUserWithCurrentWeather()
    }

    private fun scheduleWorkForNotifyingUserWithCurrentWeather() {
        _workManager.enqueue(_workRequest)
    }

    fun getWeatherAndForecastBasedOnCityName(cityName: String) =
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            _weatherAndForecastBasedOnCityNameAndLocationUseCase
                .getWeatherAndForecastBasedOnCityName(cityName).collectLatest { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let {
                                _weatherAndForecastData.value = it
                            }
                        }
                        is Resource.Error -> {
                            response.message?.let { _message.emit(it) }
                        }
                    }
                    _isLoading.emit(false)
                }
        }

    fun getWeatherAndForecastBasedOnLocation() =
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            _weatherAndForecastBasedOnCityNameAndLocationUseCase.getWeatherAndForecastBasedOnLocation()
                .collectLatest { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let {
                                _weatherAndForecastData.value = it
                            }
                        }
                        is Resource.Error -> {
                            response.message?.let { _message.emit(it) }
                        }
                    }
                    _isLoading.emit(false)
                }
        }


    fun persistTheCityWeatherData(favoriteCityWeatherEntity: FavoriteCityWeatherEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _localWeatherRepository.persistFavoriteCityData(favoriteCityWeatherEntity)
        }
    }


}
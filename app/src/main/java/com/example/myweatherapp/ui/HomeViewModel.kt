package com.example.myweatherapp.ui

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local.repository.LocalWeatherRepository
import com.example.local.room.FavoriteCityWeatherEntity
import com.example.location.repository.WeatherLocationRepository
import com.example.location.utils.LocationResource
import com.example.remote.model.WeatherAndForecastResponseData
import com.example.remote.repository.WeatherDataRepository
import com.example.remote.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shaheer cs on 04/03/2022.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _weatherDataRepository: WeatherDataRepository,
    private val _weatherLocationRepository: WeatherLocationRepository,
    private val _localWeatherRepository: LocalWeatherRepository
) : ViewModel() {

    private val _weatherAndForecastAndForecastDataData: MutableLiveData<WeatherAndForecastResponseData> =
        MutableLiveData()
    val weatherAndForecastData get() = _weatherAndForecastAndForecastDataData

    private val _locationStatus: MutableLiveData<LocationResource<Location>> = MutableLiveData()
    val locationStatus get() = _locationStatus


    init {
        getLocation()
    }

    fun getWeatherAndForecastBasedOnLocation(query: String) =
        viewModelScope.launch {
            when (val response =
                _weatherDataRepository.getWeatherAndForecastBasedOnLocation(query)) {
                is Resource.Success -> {
                    response.data?.let {
                        _weatherAndForecastAndForecastDataData.value = it
                    }
                }
                is Resource.Error -> {

                }

            }


        }

    fun getLocation() {
        viewModelScope.launch {
            _locationStatus.value = _weatherLocationRepository.getLocation()
        }
    }

    fun persistTheCityWeatherData(favoriteCityWeatherEntity: FavoriteCityWeatherEntity) {
        viewModelScope.launch {
            _localWeatherRepository.persistFavoriteCityData(favoriteCityWeatherEntity)
        }
    }


}
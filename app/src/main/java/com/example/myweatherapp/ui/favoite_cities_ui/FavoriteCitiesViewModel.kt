package com.example.myweatherapp.ui.favoite_cities_ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local.repository.LocalWeatherRepository
import com.example.local.room.FavoriteCityWeatherEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shaheer cs on 05/03/2022.
 */
@HiltViewModel
class FavoriteCitiesViewModel @Inject constructor(
    private val _localWeatherRepository: LocalWeatherRepository
) : ViewModel() {
    private val _weatherAndForecastAndForecastDataData: MutableLiveData<List<FavoriteCityWeatherEntity>> =
        MutableLiveData()
    val weatherAndForecastData get() = _weatherAndForecastAndForecastDataData

    init {
        getPersistedListOfFavoriteCities()
    }

    private fun getPersistedListOfFavoriteCities() {
        viewModelScope.launch {
            _weatherAndForecastAndForecastDataData.value =
                _localWeatherRepository.getPersistedListOfFavoriteCities()
        }
    }

}
package com.example.myweatherapp.ui.favoite_cities_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local.room.WeatherEntity
import com.example.myweatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shaheer cs on 05/03/2022.
 */
@HiltViewModel
class FavoriteCitiesViewModel @Inject constructor(
    private val _weatherRepository: WeatherRepository
) : ViewModel() {
    private val _weatherAndForecastAndForecastDataData = MutableStateFlow<List<WeatherEntity>>(emptyList())
    val weatherAndForecastData get() = _weatherAndForecastAndForecastDataData.asStateFlow()

    init {
        getPersistedListOfFavoriteCities()
    }

    private fun getPersistedListOfFavoriteCities() = viewModelScope.launch(Dispatchers.IO) {
        _weatherAndForecastAndForecastDataData.value =
            _weatherRepository.getPersistedListOfFavoriteCities()
    }
}

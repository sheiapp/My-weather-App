package com.example.local.repository

import androidx.lifecycle.LiveData
import com.example.local.room.FavoriteCityWeatherEntity
import com.example.local.room.WeatherDAO
import javax.inject.Inject

/**
 * Created by Shaheer cs on 06/03/2022.
 */
class LocalWeatherRepository @Inject constructor(private val _weatherDao: WeatherDAO) :
    LocalWeatherRepositoryImpl {
    override suspend fun persistFavoriteCityData(favoriteCityWeatherEntity: FavoriteCityWeatherEntity) =
        _weatherDao.persistFavoriteCityData(favoriteCityWeatherEntity)


    override suspend fun getPersistedListOfFavoriteCities(): List<FavoriteCityWeatherEntity> =
        _weatherDao.getPersistedListOfFavoriteCities()
}
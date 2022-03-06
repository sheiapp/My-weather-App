package com.example.local.repository

import com.example.local.room.FavoriteCityWeatherEntity

/**
 * Created by Shaheer cs on 06/03/2022.
 */
interface LocalWeatherRepositoryImpl {
    suspend fun persistFavoriteCityData(favoriteCityWeatherEntity: FavoriteCityWeatherEntity)
    suspend fun getPersistedListOfFavoriteCities(): List<FavoriteCityWeatherEntity>
}
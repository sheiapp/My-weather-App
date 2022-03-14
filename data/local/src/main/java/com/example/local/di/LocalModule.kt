package com.example.local.di

import android.content.Context
import androidx.room.Room
import com.example.local.room.WeatherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Shaheer cs on 14/03/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun weatherDatabaseProvider(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, WeatherDataBase::class.java, WeatherDataBase.DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun weatherDaoProvider(dataBase: WeatherDataBase) = dataBase.getWeatherDao()

}
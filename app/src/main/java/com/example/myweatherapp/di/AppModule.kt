package com.example.myweatherapp.di

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.local.room.WeatherDataBase
import com.example.location.WeatherLocationProvider
import com.example.myweatherapp.R
import com.example.myweatherapp.repository.WeatherRepository
import com.example.myweatherapp.repository.WeatherRepositoryImpl
import com.example.myweatherapp.usecase.WeatherAndForecastUseCase
import com.example.remote.network.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Shaheer cs on 04/03/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun weatherRepository(
        weatherApi: WeatherApi, weatherDatabase: WeatherDataBase,
        weatherLocationProvider: WeatherLocationProvider
    ): WeatherRepository =
        WeatherRepositoryImpl(weatherApi, weatherDatabase, weatherLocationProvider)

    @Provides
    @Singleton
    fun weatherAndForecastUseCaseProvider(weatherRepository: WeatherRepository) =
        WeatherAndForecastUseCase(weatherRepository)

    @Provides
    @Singleton
    fun initGlide(@ApplicationContext appContext: Context): RequestManager = Glide.with(appContext)
        .setDefaultRequestOptions(
            RequestOptions()
                .error(R.drawable.ic_cloud)
        )


    @Provides
    @Singleton
    fun workManagerProvider(@ApplicationContext appContext: Context) =
        WorkManager.getInstance(appContext)

    @Provides
    @Singleton
    fun workConstrainProvider() =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

}
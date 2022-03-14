package com.example.location.di

import android.content.Context
import com.example.location.WeatherLocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
object LocationModule {

    @Provides
    @Singleton
    fun weatherAppLocationProvider(
        @ApplicationContext context: Context, mFusedLocationClient: FusedLocationProviderClient
    ): WeatherLocationProvider {
        return WeatherLocationProvider(context, mFusedLocationClient)
    }

    @Provides
    @Singleton
    fun fusedLocationProvider(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}
package com.example.myweatherapp.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.local.repository.LocalWeatherRepository
import com.example.local.room.WeatherDAO
import com.example.local.room.WeatherDataBase
import com.example.local.utils.Constant.DATABASE_NAME
import com.example.location.repository.WeatherLocationRepository
import com.example.myweatherapp.R
import com.example.remote.repository.WeatherDataRepositoryImpl
import com.example.remote.Constants.BASE_URL
import com.example.remote.repository.WeatherDataRepository
import com.example.remote.network.WeatherApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Shaheer cs on 04/03/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun loggingInterceptorProvider(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun interceptorProvider(): Interceptor = Interceptor {
        val originalRequest = it.request()
        val builder =
            originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
        val newRequest = builder.build()
        it.proceed(newRequest)
    }

    @Provides
    @Singleton
    fun converterFactoryProvider(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun okHttpClientProvider(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
        addInterceptor(httpLoggingInterceptor)
    }.build()

    @Provides
    @Singleton
    fun retrofitProvider(
        converterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        addConverterFactory(converterFactory)
        client(okHttpClient)
    }.build()

    @Provides
    @Singleton
    fun appServiceProvider(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun weatherDataRepositoryProvider(weatherApi: WeatherApi): WeatherDataRepository {
        return WeatherDataRepositoryImpl(weatherApi)
    }

    @Provides
    @Singleton
    fun initGlide(@ApplicationContext appContext: Context): RequestManager = Glide.with(appContext)
        .setDefaultRequestOptions(
            RequestOptions()
                .error(R.drawable.ic_cloud)
        )

    @Provides
    @Singleton
    fun weatherAppLocationProvider(
        @ApplicationContext context: Context, mFusedLocationClient: FusedLocationProviderClient
    ): WeatherLocationRepository {
        return WeatherLocationRepository(context, mFusedLocationClient)
    }

    @Provides
    @Singleton
    fun fusedLocationProvider(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun weatherDatabaseProvider(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, WeatherDataBase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun weatherDaoProvider(dataBase: WeatherDataBase) = dataBase.getWeatherDao()

    @Provides
    @Singleton
    fun localWeatherRepositoryProvider(weatherDAO: WeatherDAO)= LocalWeatherRepository(weatherDAO)

}
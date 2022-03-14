package com.example.remote.di

import com.example.remote.Constants
import com.example.remote.network.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Shaheer cs on 14/03/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

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
        baseUrl(Constants.BASE_URL)
        addConverterFactory(converterFactory)
        client(okHttpClient)
    }.build()

    @Provides
    @Singleton
    fun appServiceProvider(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

}
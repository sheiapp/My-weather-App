package com.example.myweatherapp.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.myweatherapp.R
import com.example.remote.model.WeatherAndForecastResponseData
import com.example.remote.usecase.WeatherAndForecastBasedOnCityNameAndLocationUseCase
import com.example.remote.util.Resource
import com.example.myweatherapp.worker.Constants.CHANNEL_ID
import com.example.myweatherapp.worker.Constants.NOTIFICATION_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest


/**
 * Created by Shaheer cs on 06/03/2022.
 */
@HiltWorker
class DailyWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted params: WorkerParameters,
    private val _weatherAndForecastBasedOnCityNameAndLocationUseCase: WeatherAndForecastBasedOnCityNameAndLocationUseCase
) : CoroutineWorker(ctx, params) {
    private lateinit var response: Resource<WeatherAndForecastResponseData>
    override suspend fun doWork(): Result {
        _weatherAndForecastBasedOnCityNameAndLocationUseCase.getWeatherAndForecastBasedOnLocation().collectLatest { response = it }
        if (this::response.isInitialized) {
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        val contentTitle =
                            ctx.resources.getString(R.string.app_name)
                        val contentText = String.format(
                            ctx.resources.getString(R.string.notification_content_text_place_holder),
                            it.current?.tempC, it.current?.condition?.text, it.location?.region
                        )
                        startForegroundService(contentTitle, contentText)
                    }
                    return Result.success()
                }
                else ->
                    return Result.retry()
            }
        }
        return Result.failure()
    }

    private suspend fun startForegroundService(cntTitle: String, cntText: String) {
        setForeground(
            ForegroundInfo(
                NOTIFICATION_ID,
                NotificationCompat.Builder(ctx, CHANNEL_ID).apply {
                    setSmallIcon(R.mipmap.ic_launcher_round)
                    setContentTitle(cntTitle)
                    setContentText(cntText)
                }.build()
            )
        )
    }

}
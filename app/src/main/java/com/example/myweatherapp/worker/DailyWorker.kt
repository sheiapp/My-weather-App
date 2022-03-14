package com.example.myweatherapp.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.local.room.WeatherEntity
import com.example.myweatherapp.R
import com.example.myweatherapp.repository.WeatherRepository
import com.example.myweatherapp.worker.Constants.CHANNEL_ID
import com.example.myweatherapp.worker.Constants.NOTIFICATION_ID
import com.example.remote.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.TimeUnit


/**
 * Created by Shaheer cs on 06/03/2022.
 */
@HiltWorker
class DailyWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted params: WorkerParameters,
    private val _weatherRepository: WeatherRepository
) : CoroutineWorker(ctx, params) {
    private lateinit var response: Resource<WeatherEntity>
    override suspend fun doWork(): Result {
        _weatherRepository.getWeatherAndForecastBasedOnLocation().collectLatest { response = it }
        if (this::response.isInitialized) {
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        val contentTitle =
                            ctx.resources.getString(R.string.app_name)
                        val tempInSelectedUnit = if (it.isTempInCelsius) String.format(
                            ctx.resources.getString(R.string.celsius_placeholder),
                            it.tempInCelsius
                        ) else String.format(
                            ctx.resources.getString(R.string.fahrenheit),
                            it.tempInFahrenheit
                        )
                        val contentText = String.format(
                            ctx.resources.getString(R.string.notification_content_text_place_holder),
                            tempInSelectedUnit, it.conditionText, it.cityName
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

    companion object {
        fun scheduleWorkForNotifyingUserWithCurrentWeather(
            workManager: WorkManager,
            workConstraints: Constraints,
        ) {
            val workRequest = OneTimeWorkRequestBuilder<DailyWorker>().apply {
                setInitialDelay(timeDiff(), TimeUnit.MILLISECONDS)
                setConstraints(workConstraints)
            }.build()
            workManager.enqueue(workRequest)
        }
    }

}
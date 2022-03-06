package com.example.myweatherapp.Worker

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Shaheer cs on 06/03/2022.
 */
// todo worker has to implement
class DailyWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    private val _notificationTime = 6
    override suspend fun doWork(): Result {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        // Set Execution around 06:00:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, _notificationTime)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDiff = (dueDate.timeInMillis - currentDate.timeInMillis)
        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(dailyWorkRequest)
        return Result.success()
    }

}
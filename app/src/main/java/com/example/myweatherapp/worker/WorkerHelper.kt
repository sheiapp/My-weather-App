package com.example.myweatherapp.worker

import com.example.myweatherapp.worker.Constants.SCHEDULED_TIME_IN_HOURS
import com.example.myweatherapp.worker.Constants.SCHEDULED_TIME_IN_MINUTES
import com.example.myweatherapp.worker.Constants.SCHEDULED_TIME_IN_SECONDS
import java.util.*

/**
 * Created by Shaheer cs on 06/03/2022.
 */
fun timeDiff(): Long {
    val amountOfTime = 24
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()
    dueDate.set(Calendar.HOUR_OF_DAY, SCHEDULED_TIME_IN_HOURS)
    dueDate.set(Calendar.MINUTE, SCHEDULED_TIME_IN_MINUTES)
    dueDate.set(Calendar.SECOND, SCHEDULED_TIME_IN_SECONDS)
    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.HOUR_OF_DAY, amountOfTime)
    }
    return (dueDate.timeInMillis - currentDate.timeInMillis)
}

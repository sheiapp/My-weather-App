package com.example.local.model

import androidx.room.ColumnInfo

/**
 * Created by Shaheer cs on 12/03/2022.
 */
data class Forecast(
    @ColumnInfo(name = "condition_icon")
    val conditionIcon: String,
    @ColumnInfo(name = "condition")
    val conditionText: String,
    @ColumnInfo(name = "date")
    val date: String,
)

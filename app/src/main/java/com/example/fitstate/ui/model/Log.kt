package com.example.fitstate.ui.model

data class Log(
    val bodyState: BodyState,
    val movingAverage: Float,
    val weeklyRate: Float?,
)
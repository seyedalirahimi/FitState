package com.example.fitstate.ui.model

import kotlinx.datetime.Instant
import com.example.fitstate.data.database.BodyStateEntity

data class BodyState(
    val weight: Float,
    val note: String?,
    val timestamp: Instant
)

fun BodyState.toBodyStateEntity(): BodyStateEntity {
    return BodyStateEntity(weight, note, timestamp)
}
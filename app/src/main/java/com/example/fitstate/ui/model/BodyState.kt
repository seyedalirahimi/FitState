package com.example.fitstate.ui.model

import kotlinx.datetime.Instant
import org.ali.fitState.data.database.BodyStateEntity

data class BodyState(
    val id: Int = -1,
    val weight: Float,
    val note: String?,
    val timestamp: Instant
)

fun BodyState.toBodyStateEntity(): BodyStateEntity {
    return BodyStateEntity(id, weight, note, timestamp)
}
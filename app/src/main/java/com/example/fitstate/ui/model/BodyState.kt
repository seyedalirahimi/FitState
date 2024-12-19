package com.example.fitstate.ui.model

import kotlinx.datetime.Instant
import com.example.fitstate.data.database.BodyStateEntity
import java.util.Date

data class BodyState(
    val weight: Float,
    val note: String?,
    val date: Date
)

fun BodyState.toBodyStateEntity(): BodyStateEntity {
    return BodyStateEntity(weight, note, date)
}
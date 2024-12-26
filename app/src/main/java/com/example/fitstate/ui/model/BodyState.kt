package com.example.fitstate.ui.model

import com.example.fitstate.data.database.BodyStateEntity
import java.util.Date

data class BodyState(
    val weight: Float,
    val notes: String?,
    val date: Date
)

fun BodyState.toBodyStateEntity(): BodyStateEntity {
    return BodyStateEntity(weight, notes, date)
}
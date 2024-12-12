package com.example.fitstate.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fitstate.ui.model.BodyState
import kotlinx.datetime.Instant

@Entity
data class BodyStateEntity(
    val weight: Float,
    val note: String?,
    @PrimaryKey
    val timestamp: Instant
)

fun BodyStateEntity.toBodyState(): BodyState {
    return BodyState(
        weight = weight,
        note = note,
        timestamp = timestamp
    )
}
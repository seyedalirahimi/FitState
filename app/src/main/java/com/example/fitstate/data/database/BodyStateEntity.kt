package com.example.fitstate.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fitstate.ui.model.BodyState
import java.util.Date

@Entity
data class BodyStateEntity(
    val weight: Float,
    val note: String?,
    @PrimaryKey
    val date: Date
)

fun BodyStateEntity.toBodyState(): BodyState {
    return BodyState(
        weight = weight,
        notes = note,
        date = date
    )
}
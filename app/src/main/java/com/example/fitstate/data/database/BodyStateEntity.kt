package org.ali.fitState.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fitstate.ui.model.BodyState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity
data class BodyStateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weight: Float,
    val note: String?,
    val timestamp: Instant
)

fun BodyStateEntity.toBodyState(): BodyState {
    return BodyState(
        id = id,
        weight = weight,
        note = note,
        timestamp = timestamp
    )
}
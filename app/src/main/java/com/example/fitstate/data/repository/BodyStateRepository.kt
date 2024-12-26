package com.example.fitstate.data.repository

import com.example.fitstate.ui.model.BodyState
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface BodyStateRepository {
    suspend fun upsert(bodyState: BodyState)
    suspend fun getBodyStates(): Flow<List<BodyState>>
    suspend fun getBodyState(date: Date): BodyState?
    suspend fun deleteBodyState(bodyState: BodyState)
}
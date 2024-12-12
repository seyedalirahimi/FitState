package com.example.fitstate.data.repository

import com.example.fitstate.ui.model.BodyState
import kotlinx.coroutines.flow.Flow

interface BodyStateRepository {
    suspend fun upsert(bodyState: BodyState)
    suspend fun getBodyStates(): Flow<List<BodyState>>
    suspend fun deleteBodyState(bodyState: BodyState)
}
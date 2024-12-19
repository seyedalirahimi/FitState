package com.example.fitstate.data.repository

import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.model.toBodyStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ali.fitState.data.database.BodyStateDao
import com.example.fitstate.data.database.toBodyState
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class BodyStateRepositoryImpl @Inject constructor(
    private val bodyStateDao: BodyStateDao
) : BodyStateRepository {
    override suspend fun upsert(bodyState: BodyState) {
        bodyStateDao.upsert(bodyState.toBodyStateEntity())
    }

    override suspend fun getBodyStates(): Flow<List<BodyState>> {
        return bodyStateDao.getBodyStates()
            .onEach {
                println("BodyStateRepositoryImpl.getBodyStates: $it")
            }
            .map { bodyStateEntities ->
                println("BodyStateRepositoryImpl.getBodyStates: $bodyStateEntities")
                if (bodyStateEntities.isNullOrEmpty()) {
                    println("BodyStateRepositoryImpl.getBodyStates: empty list")
                    emptyList<BodyState>()
                } else {
                    bodyStateEntities.map { it.toBodyState() }
                }
            }
    }

    override suspend fun deleteBodyState(bodyState: BodyState) {
        bodyStateDao.deleteBodyState(bodyState.toBodyStateEntity())
    }

}
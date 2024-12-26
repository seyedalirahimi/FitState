package com.example.fitstate.data.repository

import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.model.toBodyStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ali.fitState.data.database.BodyStateDao
import com.example.fitstate.data.database.toBodyState
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import java.util.Date
import javax.inject.Inject


class BodyStateRepositoryImpl @Inject constructor(
    private val bodyStateDao: BodyStateDao
) : BodyStateRepository {
    override suspend fun upsert(bodyState: BodyState) {
        bodyStateDao.upsert(bodyState.toBodyStateEntity())
    }

    override suspend fun getBodyStates(): Flow<List<BodyState>> {
        return bodyStateDao.getBodyStates()
            .map { bodyStateEntities ->
                if (bodyStateEntities.isNullOrEmpty()) {
                    emptyList<BodyState>()
                } else {
                    bodyStateEntities.map { it.toBodyState() }
                }
            }
    }

    override suspend fun getBodyState(date: Date): BodyState? {
        return bodyStateDao.getBodyState(date)?.toBodyState()
    }

    override suspend fun deleteBodyState(bodyState: BodyState) {
        bodyStateDao.deleteBodyState(bodyState.toBodyStateEntity())
    }

}
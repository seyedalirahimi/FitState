package com.example.fitstate.data.repository

import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.model.toBodyStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ali.fitState.data.database.BodyStateDao
import org.ali.fitState.data.database.toBodyState
import javax.inject.Inject


class BodyStateRepositoryImpl @Inject constructor(
    private val bodyStateDao: BodyStateDao
) : BodyStateRepository {
    override suspend fun upsert(book: BodyState) {
        bodyStateDao.upsert(book.toBodyStateEntity())
    }

    override suspend fun getBodyStates(): Flow<List<BodyState>> {
        return bodyStateDao.getBodyStates().map { bodyStateEntities ->
            bodyStateEntities.map { it.toBodyState() }
        }
    }

    override suspend fun getBodyState(id: Int): BodyState? {
        return bodyStateDao.getBodyState(id)?.toBodyState()
    }

    override suspend fun deleteBodyState(id: Int) {
        bodyStateDao.deleteBodyState(id)
    }

}
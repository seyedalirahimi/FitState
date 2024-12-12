package org.ali.fitState.data.database


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyStateDao {

    @Upsert
    suspend fun upsert(book: BodyStateEntity)

    @Query("SELECT * FROM BodyStateEntity")
    fun getBodyStates(): Flow<List<BodyStateEntity>>

    @Query("SELECT * FROM BodyStateEntity WHERE id = :id")
    suspend fun getBodyState(id: Int): BodyStateEntity?

    @Query("DELETE FROM BodyStateEntity WHERE id = :id")
    suspend fun deleteBodyState(id: Int)
}
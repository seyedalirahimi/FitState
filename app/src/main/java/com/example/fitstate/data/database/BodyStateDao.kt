package org.ali.fitState.data.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.fitstate.data.database.BodyStateEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BodyStateDao {

    @Upsert
    suspend fun upsert(bodyStateEntity: BodyStateEntity)

    @Query("SELECT * FROM BodyStateEntity")
    fun getBodyStates(): Flow<List<BodyStateEntity>?>

    @Delete
    suspend fun deleteBodyState(bodyStateEntity: BodyStateEntity)

    @Query("SELECT * FROM BodyStateEntity WHERE date = :date")
    suspend fun getBodyState(date: Date): BodyStateEntity?
}
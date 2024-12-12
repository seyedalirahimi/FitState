package org.ali.fitState.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitstate.data.database.BodyStateEntity

@Database(
    entities = [BodyStateEntity::class],
    version = 1
)
@TypeConverters(InstantConverter::class)
abstract class BodyStateDatabase : RoomDatabase() {
    abstract val bodyStateDao: BodyStateDao

    companion object {
        const val DB_NAME = "fit-state.db"
    }
}
package com.example.fitstate.di

import android.content.Context
import androidx.room.Room
import com.example.fitstate.data.repository.BodyStateRepository
import com.example.fitstate.data.repository.BodyStateRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ali.fitState.data.database.BodyStateDao
import org.ali.fitState.data.database.BodyStateDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBodyStateDatabase(
        @ApplicationContext context: Context,
    ): BodyStateDatabase = Room.databaseBuilder(
        context,
        BodyStateDatabase::class.java,
        BodyStateDatabase.DB_NAME,
    ).build()

    @Provides
    fun providesBodyStateDao(bodyStateDatabase: BodyStateDatabase): BodyStateDao =
        bodyStateDatabase.bodyStateDao


    @Provides
    fun providesBodyStateRepository(bodyStateDao: BodyStateDao): BodyStateRepository =
        BodyStateRepositoryImpl(bodyStateDao)

}

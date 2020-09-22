package com.ibrahim.runningapp.di

import android.content.Context
import androidx.room.Room
import com.ibrahim.runningapp.dp.RunningDatabase
import com.ibrahim.runningapp.utils.Constance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesRunningDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, RunningDatabase::class.java,
        Constance.RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesRunDao(database: RunningDatabase)=database.getRunDau()
}
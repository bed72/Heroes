package github.bed72.bedapp.framework.di

import dagger.Module
import dagger.Provides
import androidx.room.Room
import dagger.hilt.InstallIn
import android.content.Context
import dagger.hilt.components.SingletonComponent
import github.bed72.bedapp.framework.db.AppDatabase
import github.bed72.core.data.constants.DbConstants
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DbConstants.APP_DATABASE_NAME
    ).build()
}
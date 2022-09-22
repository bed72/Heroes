package github.bed72.bedapp.framework.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.bed72.bedapp.framework.db.AppDatabase
import github.bed72.bedapp.framework.db.daos.FavoriteDao
import github.bed72.core.data.constants.DbConstants

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DbConstants.APP_DATABASE_NAME
        ).build()

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDao = appDatabase.favoriteDao()

    @Provides
    fun provideCharacterDao(appDatabase: AppDatabase) = appDatabase.characterDao()
}

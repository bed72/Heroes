package github.bed72.bedapp.framework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import github.bed72.bedapp.framework.FavoritesRepositoryImpl
import github.bed72.bedapp.framework.local.RoomFavoritesDataSource
import github.bed72.core.data.repository.favorites.FavoritesRepository
import github.bed72.core.data.repository.favorites.FavoriteLocalDataSource

@Module
@InstallIn(SingletonComponent::class)
interface FavoritesRepositoryModule {
    @Binds
    fun bindFavoriteRepository(repository: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    fun bindLocalDataSource(
        dataSource: RoomFavoritesDataSource
    ): FavoriteLocalDataSource
}
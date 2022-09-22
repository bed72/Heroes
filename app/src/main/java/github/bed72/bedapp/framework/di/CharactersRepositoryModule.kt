package github.bed72.bedapp.framework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.bed72.bedapp.framework.CharacterRepositoryImpl
import github.bed72.bedapp.framework.remote.RetrofitCharacterDataSource
import github.bed72.core.data.repository.characters.CharacterRemoteDataSource
import github.bed72.core.data.repository.characters.CharacterRepository

@Module
@InstallIn(SingletonComponent::class)
interface CharactersRepositoryModule {
    @Binds
    fun bindCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    fun bindRemoteDataSource(
        dataSource: RetrofitCharacterDataSource
    ): CharacterRemoteDataSource
}

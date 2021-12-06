package github.bed72.bedapp.framework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.bed72.bedapp.framework.CharactersRepositoryImpl
import github.bed72.bedapp.framework.network.response.DataWrapperResponse
import github.bed72.bedapp.framework.remote.RetrofitCharactersDataSource
import github.bed72.core.data.repository.CharactersRemoteDataSource
import github.bed72.core.data.repository.CharactersRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindCharacterRepository(repository: CharactersRepositoryImpl): CharactersRepository

    @Binds
    fun bindRemoteDataSource(
        dataSource: RetrofitCharactersDataSource
    ): CharactersRemoteDataSource<DataWrapperResponse>

}
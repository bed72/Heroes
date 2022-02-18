package github.bed72.bedapp.framework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.bed72.core.usecase.GetCharactersUseCase
import github.bed72.core.usecase.GetCharactersUseCaseImpl
import github.bed72.core.usecase.GetComicsUseCase
import github.bed72.core.usecase.GetComicsUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun bindGetComicsUseCase(useCase: GetComicsUseCaseImpl): GetComicsUseCase

    @Binds
    fun bindGetCharactersUseCase(useCase: GetCharactersUseCaseImpl): GetCharactersUseCase
}
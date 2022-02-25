package github.bed72.bedapp.framework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.bed72.core.usecase.GetCharactersUseCase
import github.bed72.core.usecase.GetCharactersUseCaseImpl
import github.bed72.core.usecase.GetCharacterCategoriesUse
import github.bed72.core.usecase.GetCharacterCategoriesUseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun bindGetCharactersUseCase(useCase: GetCharactersUseCaseImpl): GetCharactersUseCase

    @Binds
    fun bindCharacterCategoriesUseCase(useCase: GetCharacterCategoriesUseImpl): GetCharacterCategoriesUse
}
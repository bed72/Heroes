package github.bed72.bedapp.framework.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import kotlinx.coroutines.Dispatchers
import dagger.hilt.components.SingletonComponent
import github.bed72.core.usecase.base.AppCoroutinesDispatchers
import github.bed72.core.usecase.base.CoroutinesDispatchers

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {
    @Provides
    fun bindDispatchers(appCoroutinesDispatchers: AppCoroutinesDispatchers): CoroutinesDispatchers
}
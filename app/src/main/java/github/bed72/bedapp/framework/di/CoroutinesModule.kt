package github.bed72.bedapp.framework.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import kotlinx.coroutines.Dispatchers
import dagger.hilt.components.SingletonComponent
import github.bed72.core.usecase.base.AppCoroutinesDispatchers

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {
    @Provides
    fun provideDispatchers() = AppCoroutinesDispatchers(
        Dispatchers.IO,
        Dispatchers.Main,
        Dispatchers.Default
    )
}
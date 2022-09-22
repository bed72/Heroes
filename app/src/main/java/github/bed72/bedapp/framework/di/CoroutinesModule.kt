package github.bed72.bedapp.framework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.bed72.core.usecase.base.AppCoroutinesDispatchers
import github.bed72.core.usecase.base.CoroutinesDispatchers

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {
    @Binds
    fun bindDispatchers(appCoroutinesDispatchers: AppCoroutinesDispatchers): CoroutinesDispatchers
}

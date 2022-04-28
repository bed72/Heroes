package github.bed72.bedapp.framework.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import github.bed72.bedapp.BuildConfig
import github.bed72.bedapp.framework.di.qualifier.BaseUrl

@Module
@InstallIn(SingletonComponent::class)
object BaseUrlModule {
    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL
}
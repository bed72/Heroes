package github.bed72.bedapp.framework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.framework.imageloader.adapter.GlideImageLoader

@Module
@InstallIn(FragmentComponent::class)
interface AppModule {
    @Binds
    fun bindImageLoader(imageLoader: GlideImageLoader): ImageLoader
}
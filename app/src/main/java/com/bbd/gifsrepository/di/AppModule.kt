package com.bbd.gifsrepository.di

import android.content.Context
import com.bbd.gifsrepository.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context = appContext

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext context: Context) =
        Glide.with(context).setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_image_24)
                )
}
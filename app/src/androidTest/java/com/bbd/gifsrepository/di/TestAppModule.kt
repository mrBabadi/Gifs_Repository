package com.bbd.gifsrepository.di

import com.bbd.gifsrepository.BuildConfig
import com.bbd.gifsrepository.data.source.remote.webservice.GiphyWebServices
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("webservice")
    fun provideRetrofit(): GiphyWebServices {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .build().create(GiphyWebServices::class.java)
    }
}
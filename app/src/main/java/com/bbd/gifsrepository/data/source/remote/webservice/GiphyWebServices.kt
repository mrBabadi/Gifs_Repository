package com.bbd.gifsrepository.data.source.remote.webservice

import com.bbd.gifsrepository.BuildConfig
import com.bbd.gifsrepository.data.source.remote.api_model.RandomGifResponse
import com.bbd.gifsrepository.data.source.remote.api_model.SearchGifResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyWebServices {

    @GET("v1/gifs/random")
    suspend fun getRandomGif(@Query("api_key") apiKey: String = BuildConfig.GIPHY_API_KEY): Response<RandomGifResponse>

    @GET("v1/gifs/search")
    suspend fun searchGifs(
        @Query("api_key") apiKey: String = BuildConfig.GIPHY_API_KEY,
        @Query("q") query: String
    ): Response<SearchGifResponse>
}
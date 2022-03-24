package com.bbd.gifsrepository.data.repository

import com.bbd.gifsrepository.data.ResponseState
import com.bbd.gifsrepository.data.source.local.model.GifModel
import com.bbd.gifsrepository.data.source.local.model.SearchResults
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    suspend fun getRandomGif(): Flow<ResponseState<GifModel>>

    suspend fun searchForGifs(query: String): Flow<ResponseState<SearchResults>>
}
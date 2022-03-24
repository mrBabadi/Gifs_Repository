package com.bbd.gifsrepository.data.repository

import android.util.Log
import com.bbd.gifsrepository.data.ResponseState
import com.bbd.gifsrepository.data.source.local.model.GifModel
import com.bbd.gifsrepository.data.source.local.model.SearchResults
import com.bbd.gifsrepository.data.source.remote.webservice.GiphyWebServices
import com.bbd.gifsrepository.util.toRandomGifModel
import com.bbd.gifsrepository.util.toSearchResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val webServices: GiphyWebServices
) :
    ApiRepository, BaseRepository() {

    override suspend fun getRandomGif() = flow<ResponseState<GifModel>> {
        try {
            emit(ResponseState.Loading)
            val response = webServices.getRandomGif()
            if (response.isSuccessful) {
                emit(ResponseState.Success(response.body()?.toRandomGifModel()))
            } else {
                emit(ResponseState.Error(getFailedResponse(response)))
            }

        } catch (e: Exception) {
            emit(ResponseState.Error(errorMapper(e)))
        }
    }

    override suspend fun searchForGifs(query: String) = flow<ResponseState<SearchResults>> {
        try {
            emit(ResponseState.Loading)
                val response = webServices.searchGifs(query = query)
                if (response.isSuccessful) {
                    emit(ResponseState.Success(response.body()?.toSearchResult()))
                } else {
                    emit(ResponseState.Error(getFailedResponse(response)))
                }
        } catch (e: Exception) {
            emit(ResponseState.Error(errorMapper(e)))
        }

    }
}
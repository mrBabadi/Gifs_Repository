package com.bbd.gifsrepository

import com.bbd.gifsrepository.data.LocalException
import com.bbd.gifsrepository.data.ResponseState
import com.bbd.gifsrepository.data.enums.RatingSystem
import com.bbd.gifsrepository.data.repository.ApiRepository
import com.bbd.gifsrepository.data.source.local.model.GifModel
import com.bbd.gifsrepository.data.source.local.model.SearchResults
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException

class FakeApiRepository : ApiRepository {

    private var shouldReturnNetworkError = false
    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun getRandomGif() = flow<ResponseState<GifModel>> {
        emit(ResponseState.Loading)
        if (shouldReturnNetworkError) {
            emit(ResponseState.Error(LocalException(SocketTimeoutException())))
        } else {
            emit(
                ResponseState.Success(
                    GifModel(
                        "1",
                        "https://media2.giphy.com/banana",
                        "https://gph.is/banana",
                        "Banana",
                        RatingSystem.PG,
                        "https://media2.giphy.com/media/banana"
                    )
                )
            )
        }
    }

    override suspend fun searchForGifs(query: String) = flow<ResponseState<SearchResults>> {
        emit(ResponseState.Loading)
        if (shouldReturnNetworkError) {
            emit(ResponseState.Error(LocalException(SocketTimeoutException())))
        } else {
            emit(ResponseState.Success(SearchResults(getFakeSearchData())))
        }
    }
}
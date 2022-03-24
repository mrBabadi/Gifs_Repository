package com.bbd.gifsrepository.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bbd.gifsrepository.data.ResponseState
import com.bbd.gifsrepository.data.consts.FETCH_GIF_TIME_IN_MILL
import com.bbd.gifsrepository.data.enums.PageAction
import com.bbd.gifsrepository.data.repository.ApiRepository
import com.bbd.gifsrepository.data.source.local.model.GifModel
import com.bbd.gifsrepository.data.source.local.model.SearchResults
import com.bbd.gifsrepository.presentation.base.BaseViewModel
import com.bbd.gifsrepository.util.toSingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeGifViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : BaseViewModel() {


    var job: Job? = null


    private val _gif = MutableLiveData<ResponseState<GifModel>>()
    val gif: LiveData<ResponseState<GifModel>> = _gif.toSingleEvent()
    fun startToFetchRandomGif() {
        job = viewModelScope.launch {
            while (true) {
                apiRepository.getRandomGif().flowOn(Dispatchers.IO).collect {
                    _gif.postValue(it)
                }
                delay(FETCH_GIF_TIME_IN_MILL)
            }
        }
    }

    fun cancelFetchingGifJob() {
        job?.cancel()
    }


    private var _searchResult = MutableLiveData<ResponseState<SearchResults>>()
    val searchResult: LiveData<ResponseState<SearchResults>> = _searchResult.toSingleEvent()

    fun search(query: String) {
        if (query.isEmpty()) {
            return
        }
        viewModelScope.launch {
            apiRepository.searchForGifs(query).flowOn(Dispatchers.IO).collect {
                _searchResult.postValue(it)
            }
        }
    }

    private var _action = MutableLiveData(PageAction.RANDOM)
    val pageAction: LiveData<PageAction> = _action.toSingleEvent()

    fun setAction(action: PageAction) {
        _action.value = action
    }

    fun getAction() = _action.value

    override fun onCleared() {
        cancelFetchingGifJob()
        super.onCleared()
        job = null
    }
}
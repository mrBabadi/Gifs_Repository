package com.bbd.gifsrepository.data.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bbd.gifsrepository.data.LocalException
import com.bbd.gifsrepository.data.enums.RatingSystem
import com.bbd.gifsrepository.FakeApiRepository
import com.bbd.gifsrepository.data.source.local.model.GifModel
import com.bbd.gifsrepository.getOrAwaitValue
import com.bbd.gifsrepository.presentation.viewmodel.HomeGifViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeGifViewModelTest {


    private val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeGifViewModel

    private lateinit var repository: FakeApiRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = FakeApiRepository()
        viewModel = HomeGifViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun verifyViewModelIsNotNull() {
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun getRandomGif_returnSuccess() = runBlocking {
        viewModel.startToFetchRandomGif()
        delay(100)
        val gifModel = viewModel.gif.getOrAwaitValue().data
        assertThat(gifModel?.title).isEqualTo("Banana")
    }

    @Test
    fun getRandomGif_returnError() = runBlocking {
        repository.setShouldReturnNetworkError(true)
        viewModel.startToFetchRandomGif()
        delay(100)
        val response = viewModel.gif.getOrAwaitValue()
        assertThat(response.localException).isInstanceOf(LocalException::class.java)
    }

    @Test
    fun searchGif_Success() = runBlocking {
        viewModel.search("apple")
        delay(100)
        val result = viewModel.searchResult.getOrAwaitValue()
        assertThat(result.data?.searchItems?.find {
            it.title == "Apple"
        }).isEqualTo(
            GifModel(
                "2", "apple.com", "https://appl.com", "Apple",
                RatingSystem.G, "apple.preview"
            )
        )
    }

    @Test
    fun searchGif_Fails() = runBlocking {
        repository.setShouldReturnNetworkError(true)
        viewModel.search("apple")
        delay(100)
        val result = viewModel.searchResult.getOrAwaitValue()
        assertThat(result.localException).isInstanceOf(LocalException::class.java)
    }


}
package com.bbd.gifsrepository.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.MediumTest
import com.bbd.gifsrepository.MainCoroutineRule
import com.bbd.gifsrepository.data.source.remote.webservice.GiphyWebServices
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
class ApiRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = MainCoroutineRule()

    @Inject
    @Named("webservice")
    lateinit var webServices: GiphyWebServices

    @Inject
    lateinit var repository: ApiRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun verifyWebServiceIsNotNull() {
        assertThat(webServices).isNotNull()
    }

    @Test
    fun verifyRepositoryIsNotNull() {
        assertThat(repository).isNotNull()
    }


    @Test
    fun getRandomGif() = runBlocking {
        val actual = repository.getRandomGif().last()
        assertThat(actual.data?.id).isNotNull()
    }


    @Test
    fun searchGif() = runBlocking {
        val actual = repository.searchForGifs("banana").last()
        assertThat(actual.data?.searchItems?.size).isGreaterThan(1)
    }


}
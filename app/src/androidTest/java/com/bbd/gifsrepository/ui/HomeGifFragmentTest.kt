package com.bbd.gifsrepository.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.bbd.gifsrepository.FakeApiRepository
import com.bbd.gifsrepository.R
import com.bbd.gifsrepository.data.enums.PageAction
import com.bbd.gifsrepository.launchFragmentInHiltContainer
import com.bbd.gifsrepository.presentation.fragment.HomeGifFragment
import com.bbd.gifsrepository.presentation.viewmodel.HomeGifViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class HomeGifFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: HomeGifViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = HomeGifViewModel(FakeApiRepository())
    }

    @Test
    fun verify_viewModel_isNotNull() {
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun startTyping_pageAction_should_be_search_action() = runBlocking {

        launchFragmentInHiltContainer<HomeGifFragment>()

        onView(withId(R.id.searchInputEt)).perform(replaceText("ba")).also {
            viewModel.setAction(PageAction.SEARCH)
        }
        val pageAction = viewModel.getAction()
        assertThat(pageAction).isEqualTo(PageAction.SEARCH)
    }

    @Test
    fun pressBack_iv_should_be_random_action() = runBlocking {
        launchFragmentInHiltContainer<HomeGifFragment>()

        onView(withId(R.id.searchInputEt)).perform(replaceText("ba"))
        delay(500)
        onView(withId(R.id.backIv)).perform(click()).also {
            viewModel.setAction(PageAction.RANDOM)
        }

        val pageAction = viewModel.getAction()
        assertThat(pageAction).isEqualTo(PageAction.RANDOM)
    }
}
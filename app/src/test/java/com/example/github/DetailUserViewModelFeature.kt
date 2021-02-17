package com.example.github

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.github.core.data.Resource
import com.example.github.core.ui.model.UserPresenter
import com.example.github.detail.DetailUserViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object DetailUserViewModelFeature : Spek({

    beforeEachTest {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

        })
    }

    afterEachTest { ArchTaskExecutor.getInstance().setDelegate(null) }

    Feature("Detail User") {
        val viewModel by memoized { mock(DetailUserViewModel::class.java) }
        val detailUserObserver: Observer<Resource<UserPresenter>> by memoized { mock() }

        Scenario("getting detail user") {
            val mockDetailUser by memoized { (mock(UserPresenter::class.java)) }
            val dummyDetailUser by memoized { Resource.Success(mockDetailUser) }
            val mutableDetailUser by memoized { MutableLiveData<Resource<UserPresenter>>() }

            Given("an non-null detail user") {
                mutableDetailUser.value = dummyDetailUser
            }

            lateinit var detailUser: LiveData<Resource<UserPresenter>>

            When("getting detail user from DetailUserViewModel") {
                `when`(viewModel.detailUser).thenReturn(mutableDetailUser)
                detailUser = viewModel.detailUser
            }

            Then("it should have an non-null detail user") {
                assertNotNull(detailUser.value?.data)
            }

            Then("it should observe to verify changes") {
                viewModel.detailUser.observeForever(detailUserObserver)
                verify(detailUserObserver).onChanged(dummyDetailUser)
            }
        }

        Scenario("getting error detail user") {
            val dummyMessage = "Error"
            val dummyDetailUser by memoized { Resource.Error<UserPresenter>(dummyMessage) }
            val mutableDetailUser by memoized { MutableLiveData<Resource<UserPresenter>>() }

            Given("an error detail user") {
                mutableDetailUser.value = dummyDetailUser
            }

            lateinit var detailUser: LiveData<Resource<UserPresenter>>

            When("getting detail user from DetailUserViewModel") {
                `when`(viewModel.detailUser).thenReturn(mutableDetailUser)
                detailUser = viewModel.detailUser
            }

            Then("it should have an null detail user") {
                assertNull(detailUser.value?.data)
            }

            Then("it should have an error message") {
                assertEquals(dummyDetailUser.message, detailUser.value?.message)
            }

            Then("it should observe to verify changes") {
                viewModel.detailUser.observeForever(detailUserObserver)
                verify(detailUserObserver).onChanged(dummyDetailUser)
            }
        }
    }

    Feature("Is Favorited") {
        val viewModel by memoized { mock(DetailUserViewModel::class.java) }
        val isFavoritedObserver: Observer<Boolean> by memoized { mock() }

        Scenario("favorited") {
            val dummyIsFavorited by memoized { true }
            val mutableIsFavorited by memoized { MutableLiveData<Boolean>() }

            Given("an favorited status") {
                mutableIsFavorited.value = dummyIsFavorited
            }

            lateinit var isFavorited: LiveData<Boolean>

            When("getting is favorited from DetailUserViewModel") {
                `when`(viewModel.isFavorited).thenReturn(mutableIsFavorited)
                isFavorited = viewModel.isFavorited
            }

            Then("it should have favorited") {
                assertEquals(dummyIsFavorited, isFavorited.value)
            }

            Then("it should observe to verify changes") {
                viewModel.isFavorited.observeForever(isFavoritedObserver)
                verify(isFavoritedObserver).onChanged(dummyIsFavorited)
            }
        }

        Scenario("not favorited") {
            val dummyIsFavorited by memoized { false }
            val mutableIsFavorited by memoized { MutableLiveData<Boolean>() }

            Given("an not favorited status") {
                mutableIsFavorited.value = dummyIsFavorited
            }

            lateinit var isFavorited: LiveData<Boolean>

            When("getting is favorited from DetailUserViewModel") {
                `when`(viewModel.isFavorited).thenReturn(mutableIsFavorited)
                isFavorited = viewModel.isFavorited
            }

            Then("it should have not favorited") {
                assertEquals(dummyIsFavorited, isFavorited.value)
            }

            Then("it should observe to verify changes") {
                viewModel.isFavorited.observeForever(isFavoritedObserver)
                verify(isFavoritedObserver).onChanged(dummyIsFavorited)
            }
        }
    }
})
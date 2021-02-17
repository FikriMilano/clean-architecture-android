package com.example.github.favorite

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.github.core.ui.model.UserPresenter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals

object FavoriteViewModelFeature : Spek({

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

    Feature("List User") {
        val viewModel by memoized { mock(FavoriteViewModel::class.java) }
        val listUserObserver: Observer<List<UserPresenter>> by memoized { mock() }

        Scenario("getting list user") {
            val mockUser by memoized { mock(UserPresenter::class.java) }
            val dummyListUser by memoized { listOf(mockUser) }
            val mutableListUser by memoized { MutableLiveData<List<UserPresenter>>() }

            Given("an non-empty list user") {
                mutableListUser.value = dummyListUser
            }

            lateinit var listUser: LiveData<List<UserPresenter>>

            When("getting list user from FavoriteViewModel") {
                `when`(viewModel.listUser).thenReturn(mutableListUser)
                listUser = viewModel.listUser
            }

            Then("it should have a size of 1") {
                assertEquals(dummyListUser.size, listUser.value?.size)
            }

            Then("it should observe to verify changes") {
                viewModel.listUser.observeForever(listUserObserver)
                verify(listUserObserver).onChanged(dummyListUser)
            }
        }

        Scenario("getting empty list user") {
            val dummyListUser by memoized { listOf<UserPresenter>() }
            val mutableListUser by memoized { MutableLiveData<List<UserPresenter>>() }

            Given("an empty list user") {
                mutableListUser.value = dummyListUser
            }

            lateinit var listUser: LiveData<List<UserPresenter>>

            When("getting empty list user from FavoriteViewModel") {
                `when`(viewModel.listUser).thenReturn(mutableListUser)
                listUser = viewModel.listUser
            }

            Then("it should have a size of 0") {
                assertEquals(dummyListUser.size, listUser.value?.size)
            }

            Then("it should observe to verify changes") {
                viewModel.listUser.observeForever(listUserObserver)
                verify(listUserObserver).onChanged(dummyListUser)
            }
        }
    }
})
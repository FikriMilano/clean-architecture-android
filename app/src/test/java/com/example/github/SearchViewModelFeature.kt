package com.example.github

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.github.core.data.Resource
import com.example.github.core.ui.model.UserPresenter
import com.example.github.search.SearchViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertNull

@FlowPreview
@ExperimentalCoroutinesApi
object SearchViewModelFeature : Spek({

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
        val viewModel by memoized { mock(SearchViewModel::class.java) }
        val listUserObserver: Observer<Resource<List<UserPresenter>>> by memoized { mock() }

        Scenario("getting list user") {
            val mockUser by memoized { mock(UserPresenter::class.java) }
            val dummyListUser by memoized { Resource.Success(listOf(mockUser)) }
            val mutableListUser by memoized { MutableLiveData<Resource<List<UserPresenter>>>() }

            Given("an non-empty list user") {
                mutableListUser.value = dummyListUser
            }

            lateinit var listUser: LiveData<Resource<List<UserPresenter>>>

            When("getting list user from SearchViewModel") {
                `when`(viewModel.listUser).thenReturn(mutableListUser)
                listUser = viewModel.listUser
            }

            Then("it should have a size of 1") {
                assertEquals(dummyListUser.data?.size, listUser.value?.data?.size)
            }

            Then("it should observe to verify changes") {
                viewModel.listUser.observeForever(listUserObserver)
                verify(listUserObserver).onChanged(dummyListUser)
            }
        }

        Scenario("getting empty list user") {
            val dummyListUser by memoized { Resource.Success(listOf<UserPresenter>()) }
            val mutableListUser by memoized { MutableLiveData<Resource<List<UserPresenter>>>() }

            Given("an empty list user") {
                mutableListUser.value = dummyListUser
            }

            lateinit var listUser: LiveData<Resource<List<UserPresenter>>>

            When("getting empty list user from SearchViewModel") {
                `when`(viewModel.listUser).thenReturn(mutableListUser)
                listUser = viewModel.listUser
            }

            Then("it should have a size of 0") {
                assertEquals(dummyListUser.data?.size, listUser.value?.data?.size)
            }

            Then("it should observe to verify changes") {
                viewModel.listUser.observeForever(listUserObserver)
                verify(listUserObserver).onChanged(dummyListUser)
            }
        }

        Scenario("getting error list user") {
            val dummyMessage = "Error"
            val dummyListUser by memoized { Resource.Error<List<UserPresenter>>(dummyMessage) }
            val mutableListUser by memoized { MutableLiveData<Resource<List<UserPresenter>>>() }

            Given("an error list user") {
                mutableListUser.value = dummyListUser
            }

            lateinit var listUser: LiveData<Resource<List<UserPresenter>>>

            When("getting list user from SearchViewModel") {
                `when`(viewModel.listUser).thenReturn(mutableListUser)
                listUser = viewModel.listUser
            }

            Then("it should have an null list user") {
                assertNull(listUser.value?.data)
            }

            Then("it should have an error message") {
                assertEquals(dummyListUser.message, listUser.value?.message)
            }

            Then("it should observe to verify changes") {
                viewModel.listUser.observeForever(listUserObserver)
                verify(listUserObserver).onChanged(dummyListUser)
            }
        }
    }

    Feature("Query Result") {
        val viewModel by memoized { mock(SearchViewModel::class.java) }
        val queryResultObserver: Observer<Resource<List<UserPresenter>>> by memoized { mock() }

        Scenario("getting query result") {
            val mockUser by memoized { mock(UserPresenter::class.java) }
            val dummyQueryResult by memoized { Resource.Success(listOf(mockUser)) }
            val mutableQueryResult by memoized { MutableLiveData<Resource<List<UserPresenter>>>() }

            Given("an non-empty query result") {
                mutableQueryResult.value = dummyQueryResult
            }

            lateinit var queryResult: LiveData<Resource<List<UserPresenter>>>

            When("getting query result from SearchViewModel") {
                `when`(viewModel.queryResult).thenReturn(mutableQueryResult)
                queryResult = viewModel.queryResult
            }

            Then("it should have a size of 1") {
                assertEquals(dummyQueryResult.data?.size, queryResult.value?.data?.size)
            }

            Then("it should observe to verify changes") {
                viewModel.queryResult.observeForever(queryResultObserver)
                verify(queryResultObserver).onChanged(dummyQueryResult)
            }
        }

        Scenario("getting empty query result") {
            val dummyQueryResult by memoized { Resource.Success(listOf<UserPresenter>()) }
            val mutableQueryResult by memoized { MutableLiveData<Resource<List<UserPresenter>>>() }

            Given("an empty query result") {
                mutableQueryResult.value = dummyQueryResult
            }

            lateinit var queryResult: LiveData<Resource<List<UserPresenter>>>

            When("getting empty query result from SearchViewModel") {
                `when`(viewModel.queryResult).thenReturn(mutableQueryResult)
                queryResult = viewModel.queryResult
            }

            Then("it should have a size of 0") {
                assertEquals(dummyQueryResult.data?.size, queryResult.value?.data?.size)
            }

            Then("it should observe to verify changes") {
                viewModel.queryResult.observeForever(queryResultObserver)
                verify(queryResultObserver).onChanged(dummyQueryResult)
            }
        }

        Scenario("getting error query result") {
            val dummyMessage = "Error"
            val dummyQueryResult by memoized { Resource.Error<List<UserPresenter>>(dummyMessage) }
            val mutableQueryResult by memoized { MutableLiveData<Resource<List<UserPresenter>>>() }

            Given("an error query result") {
                mutableQueryResult.value = dummyQueryResult
            }

            lateinit var queryResult: LiveData<Resource<List<UserPresenter>>>

            When("getting query result from SearchViewModel") {
                `when`(viewModel.queryResult).thenReturn(mutableQueryResult)
                queryResult = viewModel.queryResult
            }

            Then("it should have an null query result") {
                assertNull(queryResult.value?.data)
            }

            Then("it should have an error message") {
                assertEquals(dummyQueryResult.message, queryResult.value?.message)
            }

            Then("it should observe to verify changes") {
                viewModel.queryResult.observeForever(queryResultObserver)
                verify(queryResultObserver).onChanged(dummyQueryResult)
            }
        }
    }
})
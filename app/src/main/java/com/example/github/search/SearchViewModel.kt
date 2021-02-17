package com.example.github.search

import androidx.lifecycle.*
import com.example.github.core.data.Resource
import com.example.github.core.domain.usecase.UserUseCase
import com.example.github.core.ui.model.UserPresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private var username: String? = null

    fun setUsername(username: String?) {
        this.username = username
    }

    private val _listUser = MutableLiveData(Unit)
    var listUser: LiveData<Resource<List<UserPresenter>>> = _listUser.switchMap {
        loadData()
    }

    private fun loadData(): LiveData<Resource<List<UserPresenter>>> = liveData {
        emitSource(userUseCase.getSearchListUser(username ?: "").asLiveData())
    }

    fun refresh() {
        _listUser.value = Unit
    }

    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    fun setQueryNewText(newText: String?) {
        viewModelScope.launch {
            queryChannel.send(newText ?: "")
        }
    }

    val queryResult: LiveData<Resource<List<UserPresenter>>> =
        queryChannel.asFlow()
            .debounce(300)
            .distinctUntilChanged()
            .filter {
                it.trim().isNotEmpty()
            }
            .mapLatest { username ->
                userUseCase.getSearchListUser(username).first()
            }
            .asLiveData()
}
package com.example.github.home

import androidx.lifecycle.*
import com.example.github.core.data.Resource
import com.example.github.core.domain.usecase.UserUseCase
import com.example.github.core.ui.model.UserPresenter
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private val _listUser = MutableLiveData(Unit)
    var listUser: LiveData<Resource<List<UserPresenter>>> = _listUser.switchMap {
        loadData()
    }

    private fun loadData(): LiveData<Resource<List<UserPresenter>>> = liveData {
        emitSource(userUseCase.getListUser().asLiveData())
    }

    fun refresh() {
        _listUser.value = Unit
    }
}
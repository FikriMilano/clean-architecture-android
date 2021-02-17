package com.example.github.detail

import androidx.lifecycle.*
import com.example.github.core.data.Resource
import com.example.github.core.domain.usecase.UserUseCase
import com.example.github.core.ui.model.UserPresenter
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailUserViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private var user: UserPresenter? = null

    fun setUser(user: UserPresenter?) {
        this.user = user
    }

    private val _detailUser = MutableLiveData(Unit)
    var detailUser: LiveData<Resource<UserPresenter>> = _detailUser.switchMap {
        loadDetail()
    }

    private fun loadDetail(): LiveData<Resource<UserPresenter>> = liveData {
        emitSource(userUseCase.getDetailUser(user?.username ?: "").asLiveData())
    }

    fun refreshDetail() {
        _detailUser.value = Unit
    }

    private val _isFavorited = MutableLiveData(Unit)
    var isFavorited: LiveData<Boolean> = _isFavorited.switchMap {
        loadIsFavorited()
    }

    private fun loadIsFavorited(): LiveData<Boolean> = liveData {
        emitSource(userUseCase.isFavorited(user?.id ?: 0).asLiveData())
    }

    fun refreshIsFavorited() {
        _isFavorited.value = Unit
    }

    fun insertFavoriteUser() = viewModelScope.launch {
        user?.let { userUseCase.insertFavoriteUser(it) }
    }

    fun deleteFavoriteUser() = viewModelScope.launch {
        user?.let { userUseCase.deleteFavoriteUser(it) }
    }
}
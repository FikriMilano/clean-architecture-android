package com.example.github.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.github.core.domain.usecase.UserUseCase
import com.example.github.core.ui.model.UserPresenter
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(userUseCase: UserUseCase) : ViewModel() {
    var listUser: LiveData<List<UserPresenter>> = liveData {
        emitSource(userUseCase.getListFavoriteUser().asLiveData())
    }
}
package com.example.github.core.domain.usecase

import com.example.github.core.data.Resource
import com.example.github.core.ui.model.UserPresenter
import kotlinx.coroutines.flow.Flow

interface UserUseCase {

    suspend fun getListUser(): Flow<Resource<List<UserPresenter>>>

    suspend fun getDetailUser(username: String): Flow<Resource<UserPresenter>>

    suspend fun isFavorited(id: Int): Flow<Boolean>

    suspend fun getListFavoriteUser(): Flow<List<UserPresenter>>

    suspend fun insertFavoriteUser(presenter: UserPresenter)

    suspend fun deleteFavoriteUser(presenter: UserPresenter)

    suspend fun getSearchListUser(username: String): Flow<Resource<List<UserPresenter>>>
}
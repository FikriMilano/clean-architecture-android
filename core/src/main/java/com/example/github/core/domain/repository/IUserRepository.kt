package com.example.github.core.domain.repository

import com.example.github.core.data.Resource
import com.example.github.core.domain.model.UserDomain
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    suspend fun getListUser(): Flow<Resource<List<UserDomain>>>

    suspend fun getDetailUser(username: String): Flow<Resource<UserDomain>>

    suspend fun isFavorited(id: Int): Flow<Boolean>

    suspend fun getListFavoriteUser(): Flow<List<UserDomain>>

    suspend fun insertFavoriteUser(domain: UserDomain)

    suspend fun deleteFavoriteUser(domain: UserDomain)

    suspend fun getSearchListUser(username: String): Flow<Resource<List<UserDomain>>>
}
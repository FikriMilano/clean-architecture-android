package com.example.github.core.data.source.remote

import android.util.Log
import com.example.github.core.BuildConfig
import com.example.github.core.data.source.remote.network.ApiResponse
import com.example.github.core.data.source.remote.network.ApiService
import com.example.github.core.data.source.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getListUser(): Flow<ApiResponse<List<UserResponse>>> = flow {
        try {
            val response = apiService.getListUser(BuildConfig.API_KEY)

            if (response.isNotEmpty())
                emit(ApiResponse.Success(response))
            else
                emit(ApiResponse.Empty)

        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailUser(username: String): Flow<ApiResponse<UserResponse>> = flow {
        try {
            val response = apiService.getDetailUser(BuildConfig.API_KEY, username)

            if (response.username != null)
                emit(ApiResponse.Success(response))
            else
                emit(ApiResponse.Empty)

        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getSearchListUser(username: String): Flow<ApiResponse<List<UserResponse>>> = flow {
        try {
            val response = apiService.getSearchListUser(BuildConfig.API_KEY, username, 5)

            if (response.listUser.isNotEmpty())
                emit(ApiResponse.Success(response.listUser))
            else
                emit(ApiResponse.Empty)

        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)
}
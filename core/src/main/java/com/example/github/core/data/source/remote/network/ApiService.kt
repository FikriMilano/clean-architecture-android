package com.example.github.core.data.source.remote.network

import com.example.github.core.data.source.remote.response.SearchResponse
import com.example.github.core.data.source.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getListUser(
        @Header("Authorization") auth: String
    ): List<UserResponse>

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Header("Authorization") auth: String,
        @Path("username") username: String
    ): UserResponse

    @GET("search/users")
    suspend fun getSearchListUser(
        @Header("Authorization") auth: String,
        @Query("q") username: String,
        @Query("per_page") resultAmount: Int
    ): SearchResponse
}
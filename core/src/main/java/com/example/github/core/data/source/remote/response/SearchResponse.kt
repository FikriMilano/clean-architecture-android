package com.example.github.core.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(

    @Json(name = "items")
    val listUser: List<UserResponse>
)

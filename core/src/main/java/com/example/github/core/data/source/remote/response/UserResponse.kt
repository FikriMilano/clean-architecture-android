package com.example.github.core.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(

    @Json(name = "id")
    val id: Int?,

    @Json(name = "login")
    val username: String?,

    @Json(name = "avatar_url")
    val avatarUrl: String?,

    @Json(name = "name")
    val name: String?,

    @Json(name = "company")
    val company: String?,

    @Json(name = "location")
    val location: String?,

    @Json(name = "blog")
    val blog: String?,

    @Json(name = "email")
    val email: String?,

    @Json(name = "followers")
    val followers: Int?,

    @Json(name = "following")
    val following: Int?,

    @Json(name = "public_repos")
    val publicRepos: Int?
)

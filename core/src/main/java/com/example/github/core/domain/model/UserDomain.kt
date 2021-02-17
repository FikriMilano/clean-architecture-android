package com.example.github.core.domain.model

data class UserDomain(
    val id: Int?,
    val username: String?,
    val avatarUrl: String?,
    val name: String?,
    val company: String?,
    val location: String?,
    val blog: String?,
    val email: String?,
    val followers: Int?,
    val following: Int?,
    val publicRepos: Int?
)
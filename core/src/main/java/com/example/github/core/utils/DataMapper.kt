package com.example.github.core.utils

import com.example.github.core.data.source.local.entity.FavoriteUserEntity
import com.example.github.core.data.source.local.entity.UserMinimalEntity
import com.example.github.core.data.source.remote.response.UserResponse
import com.example.github.core.domain.model.UserDomain
import com.example.github.core.ui.model.UserPresenter

object DataMapper {

    fun mapUserResponseToUserMinimalEntity(input: UserResponse): UserMinimalEntity =
        UserMinimalEntity(
            id = input.id ?: 0,
            username = input.username ?: "-",
            avatarUrl = input.avatarUrl ?: "-"
        )

    fun mapUserDomainToUserPresenter(input: UserDomain): UserPresenter =
        UserPresenter(
            id = input.id ?: 0,
            username = input.username ?: "-",
            avatarUrl = input.avatarUrl ?: "-",
            name = input.name ?: "-",
            company = input.company ?: "-",
            location = input.location ?: "-",
            blog = input.blog ?: "-",
            email = input.email ?: "-",
            followers = input.followers.toString(),
            following = input.following.toString(),
            publicRepos = input.publicRepos.toString()
        )

    fun mapUserMinimalEntityToUserDomain(input: UserMinimalEntity): UserDomain =
        UserDomain(
            id = input.id,
            username = input.username,
            avatarUrl = input.avatarUrl,
            name = "-",
            company = "-",
            location = "-",
            blog = "-",
            email = "-",
            followers = 0,
            following = 0,
            publicRepos = 0
        )

    fun mapUserResponseToUserDomain(input: UserResponse?): UserDomain =
        UserDomain(
            id = input?.id ?: 0,
            username = input?.username ?: "-",
            avatarUrl = input?.avatarUrl ?: "-",
            name = input?.name ?: "-",
            company = input?.company ?: "-",
            location = input?.location ?: "-",
            blog = input?.blog ?: "-",
            email = input?.email ?: "-",
            followers = input?.followers ?: 0,
            following = input?.following ?: 0,
            publicRepos = input?.publicRepos ?: 0
        )

    fun mapUserDomainToFavoriteUserEntity(input: UserDomain): FavoriteUserEntity =
        FavoriteUserEntity(
            id = input.id ?: 0,
            username = input.username ?: "-",
            avatarUrl = input.avatarUrl ?: "-"
        )

    fun mapUserPresenterToUserDomain(input: UserPresenter): UserDomain =
        UserDomain(
            id = input.id,
            username = input.username,
            avatarUrl = input.avatarUrl,
            name = input.name,
            company = input.company,
            location = input.location,
            blog = input.blog,
            email = input.email,
            followers = input.followers.toInt(),
            following = input.following.toInt(),
            publicRepos = input.publicRepos.toInt()
        )

    fun mapFavoriteUserEntityToUserDomain(input: FavoriteUserEntity): UserDomain =
        UserDomain(
            id = input.id,
            username = input.username,
            avatarUrl = input.avatarUrl,
            name = "-",
            company = "-",
            location = "-",
            blog = "-",
            email = "-",
            followers = 0,
            following = 0,
            publicRepos = 0
        )
}
package com.example.github.core.ui.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserPresenter(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val name: String,
    val company: String,
    val location: String,
    val blog: String,
    val email: String,
    val followers: String,
    val following: String,
    val publicRepos: String,
) : Parcelable

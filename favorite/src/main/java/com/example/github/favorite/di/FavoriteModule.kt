package com.example.github.favorite.di

import com.example.github.core.domain.usecase.UserInteractor
import com.example.github.core.domain.usecase.UserUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class FavoriteModule {

    @Binds
    abstract fun provideUserUseCase(tourismInteractor: UserInteractor): UserUseCase
}
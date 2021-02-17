package com.example.github.di

import com.example.github.core.domain.usecase.UserInteractor
import com.example.github.core.domain.usecase.UserUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun provideUserUseCase(tourismInteractor: UserInteractor): UserUseCase
}
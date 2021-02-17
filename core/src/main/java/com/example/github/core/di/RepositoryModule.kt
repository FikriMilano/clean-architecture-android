package com.example.github.core.di

import com.example.github.core.data.UserRepository
import com.example.github.core.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(tourismRepository: UserRepository): IUserRepository
}
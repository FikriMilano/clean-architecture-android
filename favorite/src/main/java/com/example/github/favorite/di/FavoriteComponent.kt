package com.example.github.favorite.di

import com.example.github.core.di.CoreComponent
import com.example.github.favorite.FavoriteFragment
import dagger.Component

@FavoriteScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [FavoriteModule::class, ViewModelModule::class]
)
interface FavoriteComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): FavoriteComponent
    }

    fun inject(fragment: FavoriteFragment)
}
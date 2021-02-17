package com.example.github.di

import com.example.github.core.di.CoreComponent
import com.example.github.detail.DetailUserFragment
import com.example.github.home.HomeFragment
import com.example.github.search.SearchFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [AppModule::class, ViewModelModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(fragment: HomeFragment)

    fun inject(fragment: DetailUserFragment)

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun inject(fragment: SearchFragment)
}
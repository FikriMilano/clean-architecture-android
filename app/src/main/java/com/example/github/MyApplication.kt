package com.example.github

import android.app.Application
import com.example.github.core.di.CoreComponent
import com.example.github.core.di.DaggerCoreComponent
import com.example.github.di.AppComponent
import com.example.github.di.DaggerAppComponent

class MyApplication : Application() {

    val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }
}
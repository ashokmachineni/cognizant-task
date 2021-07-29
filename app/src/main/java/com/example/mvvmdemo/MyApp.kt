package com.example.mvvmdemo

import android.app.Application
import com.example.mvvmdemo.di.networkModule
import com.example.mvvmdemo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class MyApp:Application(){
    companion object {
        private lateinit var instance: MyApp

        fun getInstance(): MyApp = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initApp()
    }

    private fun initApp() {
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    networkModule,
                    viewModelModule
                )
            )
        }
    }
}
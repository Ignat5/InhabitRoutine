package com.ignatlegostaev.inhabitroutine

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }

}
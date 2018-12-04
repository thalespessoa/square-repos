package com.square.repos.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.square.repos.BuildConfig

class ApplicationController : Application() {

    companion object {
        @JvmStatic
        lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder().appModule(AppModule(this)).build()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}
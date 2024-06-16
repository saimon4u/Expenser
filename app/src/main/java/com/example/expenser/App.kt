package com.example.expenser

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application(){
    companion object{
        private var instance: App? = null

        fun getInstance(): App? {
            return instance
        }
        fun getContext(): Context? {
            return instance
        }
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}
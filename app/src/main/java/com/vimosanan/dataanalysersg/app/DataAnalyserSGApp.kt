package com.vimosanan.dataanalysersg.app

import android.app.Application
import com.vimosanan.dataanalysersg.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module


class DataAnalyserSGApp: Application() {

    override fun onCreate() {
        super.onCreate()

        //start Koin Context
        startKoin {
            androidLogger()
            androidContext(this@DataAnalyserSGApp)
            modules(appModule)
        }
    }


}
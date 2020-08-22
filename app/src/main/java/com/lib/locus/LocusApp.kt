package com.lib.locus

import android.app.Application
import com.lib.locus.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LocusApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@LocusApp)
            modules(appModule)
        }
    }
}
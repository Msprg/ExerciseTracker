package com.msprg.exerciseTracker

import android.app.Application
import android.content.Context
import com.msprg.exerciseTracker.di.DataStoreModuleImpl
import com.msprg.exerciseTracker.di.IFDataStoreModule

class ExTrApplication : Application() {

    companion object {
        lateinit var datastoremodule: IFDataStoreModule
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        datastoremodule = DataStoreModuleImpl(this)
        appContext = this
    }
}

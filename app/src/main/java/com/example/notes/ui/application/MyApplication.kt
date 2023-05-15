package com.example.notes.ui.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(),Configuration.Provider {

    @Inject lateinit var workerFactory : WorkerFactory
    @Inject lateinit var hiltWorkerFactory : HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this,Configuration.Builder().setWorkerFactory(hiltWorkerFactory).build())
    }
    override fun getWorkManagerConfiguration(): Configuration{
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}
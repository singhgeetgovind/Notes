package com.singhgeetgovind.notes.ui.application

import android.app.Application
import android.content.Intent
import android.os.Process
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.singhgeetgovind.notes.ui.activity.MainActivity2
import com.singhgeetgovind.notes.ui.activity.TAG
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlin.system.exitProcess


@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider,Thread.UncaughtExceptionHandler {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    var error : MutableList<StackTraceElement> = mutableListOf()
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }
    override fun uncaughtException(p0: Thread, p1: Throwable) {
        p1.cause?.stackTrace?.forEach{ Log.e(TAG, "uncaughtException: $it") }
        error.addAll(p1.cause?.stackTrace!!)
        val intent = Intent(this, MainActivity2::class.java).apply {
            putExtra("errorDeatils", "")
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        Process.killProcess(Process.myPid())
        exitProcess(2)
    }
}
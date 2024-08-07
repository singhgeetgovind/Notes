package com.singhgeetgovind.notes.exception

import android.app.Activity
import android.content.Intent
import android.os.Process
import com.singhgeetgovind.notes.ui.activity.MainActivity2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess

fun Activity.handleUncaughtException() {
    Thread.setDefaultUncaughtExceptionHandler { _, throwable ->

        /**
        here you can report the throwable exception to Sentry or Crashlytics or whatever crash reporting service you're using,
        otherwise you may set the throwable variable to _ if it'll remain unused
         */


        val errorReport = StringBuilder()
        CoroutineScope(Dispatchers.IO).launch {
            var arr = throwable.stackTrace
            errorReport.append("---------------- Main Crash Name ----------------\n")
            errorReport.append(throwable)
            errorReport.append("\n\n")
            errorReport.append("---------------- Stack Strace ----------------\n\n")
            for (i in arr) {
                errorReport.append(i)
                errorReport.append("\n")
            }
            errorReport.append("\n---------------- end of crash deatils ----------------\n\n")

            /** If the exception was thrown in a background thread inside
            then the actual exception can be found with getCause*/
            errorReport.append("background thread Crash Log ----------------\n")
            val cause: Throwable? = throwable.cause
            if (cause != null) {
                errorReport.append("Main Crash Name - $cause".trimIndent())

                arr = cause.stackTrace
                for (i in arr) {
                    errorReport.append(i)
                    errorReport.append("\n")
                }
            }
            errorReport.append("end of background thread Crash Log ----------------\n\n")
            withContext(Dispatchers.Main) {
                val intent = Intent(this@handleUncaughtException, MainActivity2::class.java).apply {
                    putExtra("errorDeatils", errorReport.toString())
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
                Process.killProcess(Process.myPid())
                exitProcess(2)
            }

        }


    }
}

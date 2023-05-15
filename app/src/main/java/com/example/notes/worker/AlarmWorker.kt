package com.example.notes.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.notes.repository.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AlarmWorker@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val repository: Repository) : CoroutineWorker(context,workerParameters) {
    private val TAG = "AlarmWorker"

    override suspend fun doWork(): Result {
        return try {
            val intentId = inputData.getInt("IntentId",1)
            repository.updateEventTrigger(intentId)
            Result.success()
        }catch (e:Exception){
            Log.e(TAG, "doWork: ${e.message}")
            Result.failure()
        }
    }
}
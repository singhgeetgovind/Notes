package com.singhgeetgovind.notes.ui.activity


import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.ActivityMainBinding
import com.singhgeetgovind.notes.services.CustomServices
import com.singhgeetgovind.notes.shared_preferences.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val TAG: String = "MainActivity"


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var sharedPreferences : SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when (sharedPreferences.fetchSharedPrefData<Int>("Dark mode")) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
        /*appBarConfiguration = AppBarConfiguration(setOf(R.id.listFragment, R.id.apiListFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleJob(timeInMillis: Long) {
        val componentName = ComponentName(this, CustomServices::class.java)
        val jobInfo = JobInfo.Builder(150, componentName)
            .setPeriodic(timeInMillis)
            .setPersisted(true)
            .setRequiresBatteryNotLow(true)
            .build()
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        when (jobScheduler.schedule(jobInfo)) {
            JobScheduler.RESULT_SUCCESS -> {
                Log.d(TAG, "onCreate: JobSchedule success")
            }
            JobScheduler.RESULT_FAILURE -> {
                Log.d(TAG, "onCreate: JobSchedule failure")
            }
            else -> {
                Log.d(TAG, "onCreate: JobSchedule finish")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
        Log.d(TAG, "onNightModeChanged ")
    }
}
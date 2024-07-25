package com.singhgeetgovind.notes.ui.activity


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.ActivityMainBinding
import com.singhgeetgovind.notes.shared_preferences.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val TAG: String = "MainActivity"


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),NavController.OnDestinationChangedListener{
    @Inject lateinit var sharedPreferences : SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
//    private val firebaseToken by lazy { FirebaseMessaging.getInstance().token }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*        firebaseToken.addOnCompleteListener {
            if(it.isSuccessful || it.isComplete){
                Log.d(TAG, "onCreate: ${it.result}")
            }
            else{
                Log.d(TAG, "onCreate: ${it.isCanceled}")
            }
        }*/

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

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
        Log.d(TAG, "onNightModeChanged ")
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        Log.d(TAG, "onDestinationChanged: ${navController.backQueue.map { it.destination.label }}")
    }

    override fun onStop() {
        super.onStop()
        navController.removeOnDestinationChangedListener(this)
    }
}
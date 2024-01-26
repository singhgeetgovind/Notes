package com.singhgeetgovind.notes.ui.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentSettingBinding
import com.singhgeetgovind.notes.shared_preferences.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            when(sharedPreferences.fetchSharedPrefData<Int>("Dark mode")) {
                AppCompatDelegate.MODE_NIGHT_YES->darkMode.check(R.id.dark)
                AppCompatDelegate.MODE_NIGHT_NO->darkMode.check(R.id.light)
                else->darkMode.check(R.id.system_default)
            }
        }
        binding.darkMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.dark -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPreferences.saveSharedPrefData("Dark mode",AppCompatDelegate.MODE_NIGHT_YES)
                }
                R.id.light -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPreferences.saveSharedPrefData("Dark mode", AppCompatDelegate.MODE_NIGHT_NO)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    sharedPreferences.saveSharedPrefData("Dark mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
        binding.clearDataBtn.setOnClickListener {
            if(sharedPreferences.clearPreferences()) {
                Log.d("sharedPreferences", "onViewCreated: clear")
                findNavController().backQueue.filter { it.destination.id == R.id.splashFragment }.run{
                    if(isNullOrEmpty()) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.listFragment,true)
                            .build()
                        findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToSplashFragment(), navOptions)
                    }else{
                        findNavController().popBackStack(R.id.listFragment, true)
                    }
                }
            }
        }
    }

}
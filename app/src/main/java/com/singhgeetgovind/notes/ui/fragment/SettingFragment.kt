package com.singhgeetgovind.notes.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater)
        sharedPreferences = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE)
        edit = sharedPreferences.edit()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            when(sharedPreferences.getInt("Dark mode", AppCompatDelegate.MODE_NIGHT_NO)) {
                AppCompatDelegate.MODE_NIGHT_YES->darkMode.check(R.id.dark)
                AppCompatDelegate.MODE_NIGHT_NO->darkMode.check(R.id.light)
                else->darkMode.check(R.id.system_default)
            }
        }
        binding.darkMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.dark -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    edit.putInt("Dark mode",AppCompatDelegate.MODE_NIGHT_YES).apply()
                }
                R.id.light -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    edit.putInt("Dark mode", AppCompatDelegate.MODE_NIGHT_NO).apply()
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    edit.putInt("Dark mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).apply()
                }
            }
        }
    }

}
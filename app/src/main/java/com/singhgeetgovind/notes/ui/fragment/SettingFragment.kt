package com.singhgeetgovind.notes.ui.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.singhgeetgovind.notes.BuildConfig
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentSettingBinding
import com.singhgeetgovind.notes.shared_preferences.SharedPreferences
import com.singhgeetgovind.notes.viewmodels.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    @Inject lateinit var sharedPreferences: SharedPreferences
    private val viewModel: MyViewModel by activityViewModels()
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
            getDetails()
        }
        binding.topAppBarSetting.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.darkMode.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPreferences.saveSharedPrefData("Dark mode",AppCompatDelegate.MODE_NIGHT_YES)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPreferences.saveSharedPrefData("Dark mode", AppCompatDelegate.MODE_NIGHT_NO)
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

    private fun ViewBinding.getDetails(){
        (this as FragmentSettingBinding).run {
            fullName.text = sharedPreferences.fetchSharedPrefData<String>("FullName")
            loadImage()
            version.append(BuildConfig.VERSION_NAME)
            when(sharedPreferences.fetchSharedPrefData<Int>("Dark mode")) {
                AppCompatDelegate.MODE_NIGHT_YES->darkMode.isChecked = true
                AppCompatDelegate.MODE_NIGHT_NO->darkMode.isChecked = false
            }
        }
    }
    private fun ViewBinding.loadImage() {
    (this as FragmentSettingBinding)
        try {
            Glide.with(requireContext())
                .load(viewModel.profileUrl)
                .error(R.drawable.ic_baseline_account_circle_24)
                .centerCrop()
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .sizeMultiplier(0.50f)
                .addListener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e(ListFragment.TAG, "loadImage: failed")
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(ListFragment.TAG, "loadImage: ready")
                        resource?.let { profileImage.setImageDrawable(it) }
                        return true
                    }

                }).submit()

        } catch (e: Exception) {
            Log.e(ListFragment.TAG, "loadImage: ${e.message}")
        }
    }
}
package com.singhgeetgovind.notes.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentSplashBinding
import com.singhgeetgovind.notes.ui.activity.MainActivity

class SplashFragment : Fragment() {

    private lateinit var binding : FragmentSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater,null,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            (activity as MainActivity).sharedPreferences.fetchSharedPrefData<String>("FullName").apply {
                nextPageButton.isVisible = (this == null)
            }
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in_out)
            animation.repeatMode = Animation.RESTART
            animation.setAnimationListener(object : AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                    Log.d("TAG", "onAnimationStart: ")
                }

                override fun onAnimationEnd(p0: Animation?) {
                    if(!nextPageButton.isVisible){
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToListFragment())
                    }
                }

                override fun onAnimationRepeat(p0: Animation?) {
                    Log.d("TAG", "onAnimationRepeat: ")
                }

            })
            splashBackground.animation = animation

            nextPageButton.setOnClickListener {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToProfileFragment())
            }
        }
    }
}
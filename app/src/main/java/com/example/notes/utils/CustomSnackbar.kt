package com.example.notes.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object CustomSnackbar{

    fun snackBar(view: View,message:String,duration: Int= Snackbar.LENGTH_SHORT,
                 animationMode: Int= Snackbar.ANIMATION_MODE_FADE){
        val snackbar = Snackbar.make(view,message,duration)
        snackbar.apply {
            setAnimationMode(animationMode)
            show()
        }
    }

}
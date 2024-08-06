package com.singhgeetgovind.notes.ui.baseinterface

import android.view.View
import com.singhgeetgovind.notes.model.Notes

interface OnClickListener {
    fun onItemClickListener(item: Notes)
    fun onLongItemClickListener(item:Notes,view : View,position: Int) : Boolean
//    fun onItemStateChanged(key: Long, selected: Boolean)
}
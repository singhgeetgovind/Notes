package com.singhgeetgovind.notes.ui.baseinterface

import android.view.View
import com.singhgeetgovind.notes.model.Notes

interface OnClickListener {
    fun onItemClickListener(item: Notes)
    fun onLongItemClickListener(item:Notes,view : View,position: Int) : Boolean
//    fun onItemStateChanged(key: Long, selected: Boolean)
}
interface OnItemClickListener : OnClickListener{
    override fun onItemClickListener(item: Notes)
}
interface OnLongItemClickListener : OnClickListener{
    override fun onLongItemClickListener(item:Notes,view : View,position: Int) : Boolean
}
package com.singhgeetgovind.notes.ui.baseinterface

import android.view.View
import com.singhgeetgovind.notes.data.model.Notes

interface OnClickListener{
    fun onLongItemClickListener(item: Notes, view: View, position: Int): Boolean{
        return false
    }

    fun onItemClickListener(item: Notes){
        return
    }
}

interface OnLongItemClickListener : OnClickListener{
    override fun onLongItemClickListener(item: Notes, view: View, position: Int): Boolean
}
interface OnItemClickListener : OnClickListener {
    override fun onItemClickListener(item: Notes)
}
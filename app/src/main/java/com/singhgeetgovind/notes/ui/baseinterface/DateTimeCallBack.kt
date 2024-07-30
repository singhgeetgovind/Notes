package com.singhgeetgovind.notes.ui.baseinterface

import java.util.Calendar

interface DateTimeCallBack {
    fun dateClick(calendar: Calendar)
    fun timeClick(hours: Int=0, minute: Int=0)
}
interface DateCallBack : DateTimeCallBack {
    override fun dateClick(calendar: Calendar)
}
interface TimeCallBack : DateTimeCallBack {
    override fun timeClick(hours: Int, minute: Int)
}

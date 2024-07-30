package com.singhgeetgovind.notes.ui.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.singhgeetgovind.notes.ui.baseinterface.DateTimeCallBack
import java.util.Calendar
import java.util.concurrent.TimeUnit


class CustomTimePickerDialog (): DialogFragment(),TimePickerDialog.OnTimeSetListener {
    private lateinit var timeCallBack: DateTimeCallBack
    constructor(callBack: DateTimeCallBack) : this() {
        timeCallBack = callBack
    }

    private val TAG = "CustomTimePickerDialog"
    private var millis : Long = System.currentTimeMillis()
    private var hourOfDay : Int = (TimeUnit.MILLISECONDS.toHours(millis) % 24).toInt()
    private var minute : Int = (TimeUnit.MILLISECONDS.toMinutes(millis) % 60).toInt()
    private lateinit var calendar:Calendar
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar= Calendar.getInstance()
        hourOfDay=calendar.get(Calendar.HOUR)
        minute=calendar.get(Calendar.MINUTE)
        return TimePickerDialog(context,this, hourOfDay,minute,DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hourOfDay = hourOfDay
        this.minute= minute
        if(this::timeCallBack.isInitialized){ timeCallBack.timeClick(this.hourOfDay, this.minute) }
    }

}
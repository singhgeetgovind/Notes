package com.example.notes.ui.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*
import kotlin.time.Duration.Companion.hours


class CustomTimePickerDialog(private val callBack: TimeCallBack) : DialogFragment(),TimePickerDialog.OnTimeSetListener {
    private var hourOfDay : Int = setFormat()
    private val TAG = "CustomTimePickerDialog"
    private fun setFormat(): Int {
        Log.d(TAG, "setFormat: ${System.currentTimeMillis().hours}")
        return when(System.currentTimeMillis().hours){
            else-> 24
        }

    }

    private var minute : Int = 0
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

        callBack.timeClick(this.hourOfDay, this.minute)
    }
    fun getHour():Int{
        return hourOfDay
    }
    fun getMinute():Int{
        return minute
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        hourOfDay= 12
        minute = 0
    }
    interface TimeCallBack{
        fun timeClick(hours: Int?, minute: Int?)
    }
}
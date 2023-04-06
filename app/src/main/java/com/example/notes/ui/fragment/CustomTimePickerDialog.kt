package com.example.notes.ui.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*


class CustomTimePickerDialog : DialogFragment(),TimePickerDialog.OnTimeSetListener {
    private var hourOfDay : Int = 12
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
}
package com.example.notes.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import android.text.format.DateFormat
import com.example.notes.utils.*
import java.util.*


class CustomTimePickerDialog : DialogFragment(),TimePickerDialog.OnTimeSetListener {
    private var hourOfDay : Int=8
    private var minute : Int=0
    private lateinit var calendar:Calendar
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar= Calendar.getInstance()
        hourOfDay=calendar.get(Calendar.HOUR)
        minute=calendar.get(Calendar.MINUTE)
        return TimePickerDialog(context,this, hourOfDay,minute,DateFormat.is24HourFormat(activity))
    }
    @SuppressLint("SimpleDateFormat")
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
        hourOfDay= 8
        minute = 0
    }
}
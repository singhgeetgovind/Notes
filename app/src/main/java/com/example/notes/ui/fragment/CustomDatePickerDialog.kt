package com.example.notes.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat

import java.util.*

class CustomDatePickerDialog(private val dateCallBack: DateCallBack): DialogFragment(),DatePickerDialog.OnDateSetListener{
    private  val TAG = "CustomDatePickerDialog"
    private var year=0
    private var month=0
    private var day=0
    private lateinit var calendar :Calendar
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar= Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        val  datePickerDialog = DatePickerDialog(requireActivity(),this,year,month,day).apply {
            this.datePicker.minDate = System.currentTimeMillis()
        }
        return  datePickerDialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day=dayOfMonth
        this.year=year
        this.month =month
        dateCallBack.dateClick(getDate())
    }
    fun getDate():Calendar{
        val cal=Calendar.getInstance()
        return if(year==0 && month==0 && day==0){
            cal
        } else {
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)
            Log.d(TAG,"getCreatedDate: ${
                SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).format(cal.timeInMillis)}")
            cal
        }
    }
    interface DateCallBack{
        fun dateClick(calendar: Calendar)
    }
}
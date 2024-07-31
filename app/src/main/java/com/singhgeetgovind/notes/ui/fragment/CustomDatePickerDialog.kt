package com.singhgeetgovind.notes.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.ui.baseinterface.DateTimeCallBack
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

class CustomDatePickerDialog(): DialogFragment(),DatePickerDialog.OnDateSetListener{

    private lateinit var dateCallBack: DateTimeCallBack
    constructor(callBack: DateTimeCallBack) : this(){
        this.dateCallBack = callBack
    }

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
        val  datePickerDialog = DatePickerDialog(requireContext(), R.style.CustomDatePicker,this,year,month,day).apply {
            this.datePicker.minDate = System.currentTimeMillis()
        }

        return  datePickerDialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day=dayOfMonth
        this.year=year
        this.month =month
/*        val bundle = Bundle()
        bundle.putParcelable("Calendar",ParcelableCalendar(getDate()))
        setFragmentResult("DateTime",bundle)*/

        if(this::dateCallBack.isInitialized){ dateCallBack.dateClick(getDate()) }
    }
    private fun getDate():Calendar{
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
}
@Parcelize
data class ParcelableCalendar(val cal : Calendar):Parcelable
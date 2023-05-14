package com.example.notes.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.*
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.R
import com.example.notes.databinding.FragmentUpdateBinding
import com.example.notes.model.Notes
import com.example.notes.services.cancelAlarm
import com.example.notes.services.scheduleEvent
import com.example.notes.utils.CustomSnackbar
import com.example.notes.utils.Utils
import com.example.notes.viewmodels.MyViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdateFragment : Fragment(), RadioGroup.OnCheckedChangeListener,BetterLinkMovementMethod.OnLinkClickListener,
    CustomTimePickerDialog.TimeCallBack,CustomDatePickerDialog.DateCallBack {

    private val TAG = "UpdateFragment"
    private var date: Long?=null
    set(value) {
        binding.scheduledTime.isVisible = value!=null
        field = value
    }
    private lateinit var betterLinkMovementMethod: BetterLinkMovementMethod
    private lateinit var dateFloatButton: FloatingActionButton
    private lateinit var timeFloatButton: FloatingActionButton
    private lateinit var cancelAlarmButton: FloatingActionButton
    private lateinit var addExtendFloatButton: ExtendedFloatingActionButton
    private lateinit var customDatePickerDialogFragment: CustomDatePickerDialog
    private lateinit var customTimePickerDialogFragment: CustomTimePickerDialog
    private lateinit var binding: FragmentUpdateBinding
    private var isAllFabAvailable = false
    private val myViewModel: MyViewModel by activityViewModels()
    private val args: UpdateFragmentArgs by navArgs()
    private var isActive = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        customDatePickerDialogFragment = CustomDatePickerDialog(this)
        customTimePickerDialogFragment = CustomTimePickerDialog(this)
        betterLinkMovementMethod = BetterLinkMovementMethod.newInstance()
        binding.upTitle.setText(args.parcelableNotes?.title)
        binding.upDescription.setText(args.parcelableNotes?.description)
        args.parcelableNotes?.eventDate?.let {
            if(it!=0L) {
                date = it
                val formattedDate = SimpleDateFormat("EEE dd MMM,hh:mm a", Locale.ENGLISH).format(Date(date?:0L))
                binding.scheduledTime.text = formattedDate
            }
        }

        isActive = args.parcelableNotes?.hasPriority ?: 0
        checkArgs(isActive)

        with(binding){
            topMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.saveButton -> {
                        onClick()
                        true
                    }
                    R.id.dateButton -> {
                        scheduleDate()
                        true
                    }
                    R.id.timeButton -> {
                        scheduleTime()
                        true
                    }
                    else -> false
                }
            }
            topMenu.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            upTitle.doOnTextChanged { text, _, _, _ ->
                topMenu.menu.findItem(R.id.saveButton).setEnabled(
                    !text?.trim().isNullOrBlank() && !binding.upTitle.text?.trim().isNullOrBlank()
                )
            }
            upTitle.doOnTextChanged { text, _, _, _ ->
                topMenu.menu.findItem(R.id.saveButton).setEnabled(
                    !text?.trim().isNullOrBlank() && !binding.upDescription.text?.trim()
                        .isNullOrBlank()
                )
            }
            upDescription.apply {
                linksClickable = true
                isClickable = true
                movementMethod = betterLinkMovementMethod
                Linkify.addLinks(this, Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES)
            }
            scheduledTime.setOnCloseIconClickListener {
                date = null
            }
        }
    }

    private fun scheduleDate() {
        customDatePickerDialogFragment.show(requireActivity().supportFragmentManager, "Calendar")
    }

    private fun scheduleTime() {
        customTimePickerDialogFragment.show(requireActivity().supportFragmentManager, "Time")
    }



    private fun checkArgs(value: Int) {
        when (value) {
            -1 -> {
                binding.upLowPriority.isChecked = true
                return
            }
            0 -> {
                binding.upMediumPriority.isChecked = true
                return
            }
            else -> {
                binding.upHighPriority.isChecked = true
                return
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun onClick() {
        val title = binding.upTitle.text
        val description = binding.upDescription.text
        if (!title.isNullOrEmpty() && !description.isNullOrEmpty()){
                val intentId = args.parcelableNotes?.intentId ?: System.currentTimeMillis().toString().substring(5,11).toInt()
                myViewModel.updateData(
                    Notes(
                        id = args.parcelableNotes?.id!!,
                        title = title.toString(),
                        description = description.toString(),
                        hasPriority = isActive,
                        createdDate = Date(System.currentTimeMillis()),
                        eventDate = date,
                        intentId = intentId
                    )
                )
                date?.let {
                    scheduleEvent(
                        requireContext(),
                        title.toString(), description.toString(),
                        date!!, intentId
                    )
                }
                args.parcelableNotes?.let {
                    Toast.makeText(requireContext(), "in $date", Toast.LENGTH_SHORT).show()
                    if(date==null) {
                        cancelAlarm(requireContext(), it.intentId ?: 0)
                        Toast.makeText(requireContext(), "out ${it.intentId}", Toast.LENGTH_SHORT).show()
                    }
                }
                findNavController().popBackStack()
            } else CustomSnackbar.snackBar(binding.root,"Please have some message")
        }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.up_low_priority -> {
                isActive = -1
                return
            }
            R.id.up_medium_priority -> {
                isActive = 0
                return
            }
            R.id.up_high_priority -> {
                isActive = 1
                return
            }
        }
    }

    override fun onClick(textView: TextView?, url: String?): Boolean {
        return false
    }

    override fun dateClick(calendar: Calendar){
        date = Utils.getInMilliSecond(
            calendar,
            customTimePickerDialogFragment.getHour(),
            customTimePickerDialogFragment.getMinute()
        )
        val formattedDate = SimpleDateFormat("EEE dd MMM,hh:mm a", Locale.ENGLISH).format(Date(date?:0L))
        binding.scheduledTime.text = formattedDate
        Log.d(TAG, "dateClick: $date")
    }

    override fun timeClick(hours: Int?, minute: Int?) {
        date = Utils.getInMilliSecond(
            customDatePickerDialogFragment.getDate(),
            hours ?: customTimePickerDialogFragment.getHour(),
            minute ?: customTimePickerDialogFragment.getMinute()
        )
        val formattedDate = SimpleDateFormat("EEE dd MMM,hh:mm a", Locale.ENGLISH).format(Date(date?:0L))
        binding.scheduledTime.text = formattedDate
        Log.d(TAG, "timeClick: $date")
    }
}


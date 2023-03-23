package com.example.notes.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.R
import com.example.notes.databinding.FragmentUpdateBinding
import com.example.notes.model.Notes
import com.example.notes.viewmodels.MyViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateFragment : Fragment(), RadioGroup.OnCheckedChangeListener {
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
        binding = FragmentUpdateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        customDatePickerDialogFragment = CustomDatePickerDialog()
        customTimePickerDialogFragment = CustomTimePickerDialog()
        addExtendFloatButton = binding.update
        dateFloatButton = binding.scheduleDate
        timeFloatButton = binding.scheduleTime
        cancelAlarmButton = binding.cancelAlarm
        binding.upTitle.setText(args.name)
        binding.upDescription.setText(args.description)
        isActive = args.isActive
        checkArgs(isActive)

        /*binding.upPriorityGrp.setOnCheckedChangeListener(this)
        addExtendFloatButton.shrink()

        addExtendFloatButton.setOnClickListener {
            showButton()
        }
        dateFloatButton.setOnClickListener {
            scheduleDate(it)
        }
        timeFloatButton.setOnClickListener {
            scheduleTime(it)
        }
        cancelAlarmButton.setOnClickListener {
            alarm(false, activity)
        }*/
        binding.topMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.saveButton -> {
                    onClick()
                    true
                }
                else-> false
            }
        }
    }

    private fun scheduleDate(view: View) {
        customDatePickerDialogFragment.show(requireActivity().supportFragmentManager, "Calendar")
    }

    private fun scheduleTime(view: View) {
        customTimePickerDialogFragment.show(requireActivity().supportFragmentManager, "Time")
    }

    private fun showButton() {
        if (!isAllFabAvailable) {
            addExtendFloatButton.extend()
            dateFloatButton.show()
            timeFloatButton.show()
            dateFloatButton.visibility = View.VISIBLE
            timeFloatButton.visibility = View.VISIBLE
            cancelAlarmButton.visibility = View.VISIBLE
            isAllFabAvailable = true
        } else {
            addExtendFloatButton.shrink()
            dateFloatButton.hide()
            timeFloatButton.hide()
            dateFloatButton.visibility = View.INVISIBLE
            timeFloatButton.visibility = View.INVISIBLE
            cancelAlarmButton.visibility = View.INVISIBLE
            isAllFabAvailable = false

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> {
                onClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
        if (!title.isNullOrEmpty()) {
            if (description.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Enter Description", Toast.LENGTH_SHORT).show()
            } else {
                myViewModel.updateData(
                    Notes(
                        id = args.id,
                        title = title.toString(),
                        description = description.toString(),
                        hasPriority = isActive,
                        date = Date(System.currentTimeMillis()),
                        eventDate = Date(getInMilliSecond())
                    )
                )
//                alarm(true, requireActivity(), title.toString(), description.toString(), getInMilliSecond())
                Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        } else {
            Toast.makeText(requireContext(), "Enter Title", Toast.LENGTH_SHORT).show()
        }
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

    @SuppressLint("SimpleDateFormat")
    private fun getInMilliSecond(): Long {
        val calendar = customDatePickerDialogFragment.getDate()
        calendar.set(Calendar.HOUR, customTimePickerDialogFragment.getHour())
        calendar.set(Calendar.MINUTE, customTimePickerDialogFragment.getMinute())
        calendar.set(Calendar.SECOND, 0)

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        return if (calendar.timeInMillis < 0) {
            args.date
        } else {
            calendar.timeInMillis
        }
    }
}


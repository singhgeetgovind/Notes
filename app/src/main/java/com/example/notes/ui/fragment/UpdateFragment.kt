package com.example.notes.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.util.Linkify
import android.view.*
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.R
import com.example.notes.databinding.FragmentUpdateBinding
import com.example.notes.model.Notes
import com.example.notes.services.alarm
import com.example.notes.utils.Utils.getInMilliSecond
import com.example.notes.viewmodels.MyViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import java.util.*

@AndroidEntryPoint
class UpdateFragment : DialogFragment(), RadioGroup.OnCheckedChangeListener,BetterLinkMovementMethod.OnLinkClickListener {
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
        binding = FragmentUpdateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        customDatePickerDialogFragment = CustomDatePickerDialog()
        customTimePickerDialogFragment = CustomTimePickerDialog()
        betterLinkMovementMethod = BetterLinkMovementMethod.newInstance()
        binding.upTitle.setText(args.name)
        binding.upDescription.setText(args.description)
        isActive = args.isActive
        checkArgs(isActive)

        binding.topMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.saveButton -> {
                    onClick()
                    true
                }
                R.id.dateButton->{
                    scheduleDate()
                    true
                }
                R.id.timeButton->{
                    scheduleTime()
                    true
                }
                else-> false
            }
        }
        binding.topMenu.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.upDescription.apply {
            linksClickable = true
            isClickable = true
            movementMethod = betterLinkMovementMethod
            Linkify.addLinks(this, Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES)
        }

    }

    private fun scheduleDate() {
        customDatePickerDialogFragment.show(requireActivity().supportFragmentManager, "Calendar")
    }

    private fun scheduleTime() {
        customTimePickerDialogFragment.show(requireActivity().supportFragmentManager, "Time")
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
                val date = getInMilliSecond(customDatePickerDialogFragment.getDate(),
                    customTimePickerDialogFragment.getHour(),customTimePickerDialogFragment.getMinute())
                myViewModel.updateData(
                    Notes(
                        id = args.id,
                        title = title.toString(),
                        description = description.toString(),
                        hasPriority = isActive,
                        createdDate = Date(System.currentTimeMillis()),
                        eventDate = Date(date)
                    )
                )
                if(date.toString().isNotBlank()) alarm(true, requireContext(), title.toString(), description.toString(), date)
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

    override fun onClick(textView: TextView?, url: String?): Boolean {
        return false
    }
}


package com.singhgeetgovind.notes.ui.fragment

import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentEditBinding
import com.singhgeetgovind.notes.services.cancelAlarm
import com.singhgeetgovind.notes.services.scheduleEvent
import com.singhgeetgovind.notes.ui.baseinterface.DateTimeCallBack
import com.singhgeetgovind.notes.utils.NotesAction
import com.singhgeetgovind.notes.viewmodels.EditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import java.util.Calendar


@AndroidEntryPoint
class EditFragment : Fragment(),DateTimeCallBack,
    Toolbar.OnMenuItemClickListener {

    private lateinit var binding : FragmentEditBinding
    private val betterLinkMovementMethod: BetterLinkMovementMethod by lazy { BetterLinkMovementMethod.getInstance() }
    private lateinit var customDatePickerDialogFragment: CustomDatePickerDialog
    private lateinit var customTimePickerDialogFragment: CustomTimePickerDialog
    private val args : EditFragmentArgs by navArgs()
    private val editViewModel : EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(args.parcelableNotes!=null){
            editViewModel.setAction(NotesAction.Update(args.parcelableNotes))
        }
        with(binding) {
            title.setText(editViewModel.title)
            description.setText(editViewModel.description)
            setDateText(editViewModel.formattedDate)
        }
        viewLifecycleOwner.lifecycleScope.launch{

            repeatOnLifecycle(Lifecycle.State.RESUMED){
                editViewModel.listenValidation.collect {
                    binding.topMenu.menu.findItem(R.id.saveButton).isEnabled = it
                }
            }
        }
        with(binding){
            topMenu.setOnMenuItemClickListener(this@EditFragment)
            topMenu.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            description.doOnTextChanged { text, _, _, _ ->
                editViewModel.setDescription(text?.trim().toString())
            }
            title.doOnTextChanged { text, _, _, _ ->
                editViewModel.setTitle(text?.trim().toString())
            }
            description.apply {
                linksClickable = true
                isClickable = true
                movementMethod = betterLinkMovementMethod
                Linkify.addLinks(this, Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES)
            }
            scheduledTime.setOnCloseIconClickListener {
                editViewModel.date = null
                setDateText(editViewModel.formattedDate)
            }
        }
    }
    private fun onClick() {
        when (editViewModel.notesAction.value) {
            is NotesAction.Add -> {
                addAndUpdate()
            }
            is NotesAction.Update<*> -> {
                cancelAlarm(requireContext(), (editViewModel.intentId))
                addAndUpdate()
            }
        }
    }

    private fun addAndUpdate() {
        with(editViewModel){
            saveData()
            if (date!=null && date!=0L) {
                scheduleEvent(
                    requireContext(),
                    title,
                    description,
                    date!!,
                    intentId
                )
            }
        }
        findNavController().popBackStack()
    }

    override fun dateClick(calendar: Calendar){
        editViewModel.calendar = calendar
        val formattedDate = editViewModel.setDateTime()
        setDateText(formattedDate)
    }

    override fun timeClick(hours: Int, minute: Int) {
        editViewModel.hour = hours
        editViewModel.minute = minute
        val formattedDate = editViewModel.setDateTime()
        setDateText(formattedDate)
    }

    private fun setDateText(formattedDate: String) {
        binding.scheduledTime.apply {
            isVisible = formattedDate.isNotBlank()
            text = formattedDate
        }
    }

    private fun scheduleDate() {
        if(this::customDatePickerDialogFragment.isInitialized){
            customDatePickerDialogFragment.show(this.parentFragmentManager, "Calendar")
        }
        else {
            customDatePickerDialogFragment = CustomDatePickerDialog(this)
            customDatePickerDialogFragment.show(this.parentFragmentManager, "Calendar")
        }
    }

    private fun scheduleTime() {
        if(this::customDatePickerDialogFragment.isInitialized){
            customTimePickerDialogFragment.show(this.parentFragmentManager, "Time")
        }
        else {
            customTimePickerDialogFragment = CustomTimePickerDialog(this)
            customTimePickerDialogFragment.show(this.parentFragmentManager, "Time")
        }

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
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

}

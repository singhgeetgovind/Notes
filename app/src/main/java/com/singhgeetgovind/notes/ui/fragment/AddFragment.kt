package com.singhgeetgovind.notes.ui.fragment


import android.annotation.SuppressLint
import android.app.*
import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentAddBinding
import com.singhgeetgovind.notes.model.Notes
import com.singhgeetgovind.notes.services.scheduleEvent
import com.singhgeetgovind.notes.utils.CustomSnackbar
import com.singhgeetgovind.notes.utils.Utils.getInMilliSecond
import com.singhgeetgovind.notes.viewmodels.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddFragment : DialogFragment(), MenuProvider,BetterLinkMovementMethod.OnLinkClickListener,
    CustomTimePickerDialog.TimeCallBack, CustomDatePickerDialog.DateCallBack,
    Toolbar.OnMenuItemClickListener {
    private val TAG = "AddFragment"
    private lateinit var betterLinkMovementMethod: BetterLinkMovementMethod
    private lateinit var binding: FragmentAddBinding
    private val myViewModel: MyViewModel by activityViewModels()
    private var isActive = -1
    private var date:Long?=null
    set(value) {
        binding.scheduledTime.isVisible = value!=null
        field = value
    }
    private val customDatePickerDialogFragment: CustomDatePickerDialog by lazy{CustomDatePickerDialog(this)}
    private val customTimePickerDialogFragment: CustomTimePickerDialog by lazy{CustomTimePickerDialog(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        betterLinkMovementMethod = BetterLinkMovementMethod.newInstance()
        with(binding){
            topMenu.setOnMenuItemClickListener(this@AddFragment)
            topMenu.menu.findItem(R.id.saveButton).isEnabled = !title.text?.trim().isNullOrBlank() && !description.text?.trim().isNullOrBlank()
            description.doOnTextChanged { text, _,_,_ ->
                topMenu.menu.findItem(R.id.saveButton).setEnabled(!text?.trim().isNullOrBlank() && !binding.title.text?.trim().isNullOrBlank())
            }
            title.doOnTextChanged { text, _,_,_ ->
                topMenu.menu.findItem(R.id.saveButton).setEnabled( !text?.trim().isNullOrBlank() && !binding.description.text?.trim().isNullOrBlank())
            }
            topMenu.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            description.apply {
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


    private fun onClick() {
        val title = binding.title.text
        val description = binding.description.text
        if (!title.isNullOrBlank() && !description.isNullOrBlank()) {
//            binding.titleInputLayout.isErrorEnabled = false
                val intentId = System.currentTimeMillis().toString().substring(5,11).toInt()
                myViewModel.addData(
                    Notes(
                        0,
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
                        title.toString(),
                        description.toString(),
                        it,
                        intentId
                    )
                }
                findNavController().popBackStack()
        } else CustomSnackbar.snackBar(binding.root,"Please have some message")
    }

    private fun scheduleDate() {
        customDatePickerDialogFragment.show(requireActivity().supportFragmentManager, "Calendar")
    }

    private fun scheduleTime() {
        customTimePickerDialogFragment.show(requireActivity().supportFragmentManager, "Time")
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        Toast.makeText(requireContext(), "menu", Toast.LENGTH_SHORT).show()
        menuInflater.inflate(R.menu.action_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveButton -> {
                Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show()
                onClick()
                true
            }
            else -> false
        }
    }

    override fun dateClick(calendar: Calendar){
        date = getInMilliSecond(calendar,
            customTimePickerDialogFragment.getHour(),
            customTimePickerDialogFragment.getMinute())
        val formattedDate = SimpleDateFormat("EEE dd MMM,hh:mm a", Locale.ENGLISH).format(Date(date?:0L))
        binding.scheduledTime.text = formattedDate
        Log.d(TAG, "dateClick: ${customTimePickerDialogFragment.getHour()} ${customTimePickerDialogFragment.getMinute()}")
    }

    override fun timeClick(hours: Int?, minute: Int?) {
        date = getInMilliSecond(customDatePickerDialogFragment.getDate(),
            hours?: customTimePickerDialogFragment.getHour(),
            minute?:customTimePickerDialogFragment.getMinute())
        val formattedDate = SimpleDateFormat("EEE dd MMM,hh:mm a", Locale.ENGLISH).format(Date(date?:0L))
        binding.scheduledTime.text = formattedDate
        Log.d(TAG, "timeClick: $date $formattedDate")
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

    override fun onClick(textView: TextView?, url: String?): Boolean = false

}
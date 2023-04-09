package com.example.notes.ui.fragment


import android.annotation.SuppressLint
import android.app.*
import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.MenuProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentAddBinding
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
class AddFragment : DialogFragment(),MenuProvider,BetterLinkMovementMethod.OnLinkClickListener {

    private lateinit var betterLinkMovementMethod: BetterLinkMovementMethod
    private val binding: FragmentAddBinding by lazy { FragmentAddBinding.inflate(layoutInflater) }
    private val myViewModel: MyViewModel by activityViewModels()
    private var isActive = -1
    private lateinit var addExtendFloatButton: ExtendedFloatingActionButton
    private lateinit var dateFloatButton: FloatingActionButton
    private lateinit var timeFloatButton: FloatingActionButton
    private lateinit var cancelAlarmButton: FloatingActionButton
    private var isAllFabAvailable = false
    private val customDatePickerDialogFragment: CustomDatePickerDialog by lazy{CustomDatePickerDialog()}
    private val customTimePickerDialogFragment: CustomTimePickerDialog by lazy{CustomTimePickerDialog()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        betterLinkMovementMethod = BetterLinkMovementMethod.newInstance()
//        activity?.addMenuProvider(this)

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
            description.apply {
                linksClickable = true
                isClickable = true
                movementMethod = betterLinkMovementMethod
                Linkify.addLinks(this, Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES)
            }
        }
    }


    private fun onClick() {
        val title = binding.title.text
        val description = binding.description.text
        if (!title.isNullOrBlank()) {
//            binding.titleInputLayout.isErrorEnabled = false

            if (description.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Enter Description", Toast.LENGTH_SHORT).show()
            } else {
                val date = getInMilliSecond(customDatePickerDialogFragment.getDate(),
                    customTimePickerDialogFragment.getHour(),
                    customTimePickerDialogFragment.getMinute())
                myViewModel.addData(
                    Notes(
                        0,
                        title = title.toString(),
                        description = description.toString(),
                        hasPriority = isActive,
                        createdDate = Date(System.currentTimeMillis()),
                        eventDate = Date(date)
                    )
                )
                if(date.toString().isNotBlank()) alarm(true,requireContext(), title.toString(),description.toString(),date)
                findNavController().popBackStack()
            }
        } else {
            Toast.makeText(requireContext(), "Enter Title", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scheduleDate() {
        customDatePickerDialogFragment.show(requireActivity().supportFragmentManager, "Calendar")
    }

    private fun scheduleTime() {
        customTimePickerDialogFragment.show(requireActivity().supportFragmentManager, "Time")
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        Toast.makeText(requireContext(), "menu", Toast.LENGTH_SHORT).show()
        menuInflater.inflate(R.menu.top_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveButton -> {
                Toast.makeText(requireContext(), "cleck", Toast.LENGTH_SHORT).show()
                onClick()
                true
            }
            else -> false
        }
    }

    override fun onClick(textView: TextView?, url: String?): Boolean {
        return false
    }

}
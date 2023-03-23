package com.example.notes.ui.fragment


import android.annotation.SuppressLint
import android.app.*
import android.os.Build
import android.os.Bundle
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
import com.example.notes.viewmodels.MyViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddFragment : DialogFragment(),MenuProvider {

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
        addExtendFloatButton = binding.add
        dateFloatButton = binding.scheduleDate
        timeFloatButton = binding.scheduleTime
        cancelAlarmButton = binding.cancelAlarm

//        activity?.addMenuProvider(this)

        binding.topMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.saveButton -> {
                    onClick()
                    dismiss()
                    true
                }
                else-> false
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
                myViewModel.addData(
                    Notes(
                        0,
                        title = title.toString(),
                        description = description.toString(),
                        hasPriority = isActive,
                        date = Date(System.currentTimeMillis()),
                        eventDate = Date(getInMilliSecond())
                    )
                )
//                alarm(true,requireActivity(), title.toString(),description.toString(),getInMilliSecond())
                Toast.makeText(context,"Data added",Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        } else {
            Toast.makeText(requireContext(), "Enter Title", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scheduleDate(view: View) {
        customDatePickerDialogFragment.show(requireActivity().supportFragmentManager, "Calendar")
    }

    private fun scheduleTime(view: View) {
        customTimePickerDialogFragment.show(requireActivity().supportFragmentManager, "Time")
    }


    @SuppressLint("SimpleDateFormat")
    private fun getInMilliSecond(): Long {
        val calendar = customDatePickerDialogFragment.getDate()
        calendar.set(Calendar.HOUR, customTimePickerDialogFragment.getHour())
        calendar.set(Calendar.MINUTE, customTimePickerDialogFragment.getMinute())
        calendar.set(Calendar.SECOND, 0)

        if(calendar.before(Calendar.getInstance()) ){
            calendar.add(Calendar.DATE,1)
        }
        return calendar.timeInMillis
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

}

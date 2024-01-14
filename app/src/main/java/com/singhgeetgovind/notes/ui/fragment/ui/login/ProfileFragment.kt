package com.singhgeetgovind.notes.ui.fragment.ui.login

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.singhgeetgovind.notes.databinding.FragmentProfileBinding
import com.singhgeetgovind.notes.shared_preferences.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BottomSheetDialogFragment() {
    @Inject lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fullName.editText?.doAfterTextChanged {
            if(!it?.trim().isNullOrBlank()){
                binding.login.isEnabled = true
            }
        }

        binding.login.setOnClickListener {
            sharedPreferences.saveSharedPrefData("FullName",
                binding.fullName.editText?.text?.trim().toString()
            )
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToListFragment())
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
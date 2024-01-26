package com.singhgeetgovind.notes.ui.fragment.ui.login

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentProfileBinding
import com.singhgeetgovind.notes.shared_preferences.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BottomSheetDialogFragment() {
    @Inject lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentProfileBinding? = null
    lateinit var uploadFileLauncher_: ActivityResultLauncher<Intent?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadFileLauncher_ = extracted()
    }
    private fun extracted(): ActivityResultLauncher<Intent?> {
        val uploadFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (it.data?.data != null) {
                        Glide.with(requireContext())
                            .load(it.data?.data.toString())
                            .centerCrop()
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .into(binding.profileImage)
                        sharedPreferences.saveSharedPrefData("ProfileImage",
                            it.data?.data.toString()

                        )
                    }
                }
            }
        return uploadFileLauncher
    }

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
        binding.pickImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.type = "image/*"
            uploadFileLauncher_.launch(intent)
        }

        binding.login.setOnClickListener {
            sharedPreferences.saveSharedPrefData("FullName",
                binding.fullName.editText?.text?.trim().toString()
            )
            val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.splashFragment,true)
            .build()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToListFragment(),navOptions)
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
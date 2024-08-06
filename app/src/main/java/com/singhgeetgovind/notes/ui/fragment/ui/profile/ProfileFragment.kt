package com.singhgeetgovind.notes.ui.fragment.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentProfileBinding
import com.singhgeetgovind.notes.shared_preferences.SharedPreferences
import com.singhgeetgovind.notes.ui.adapter.ProfileAdapter
import com.singhgeetgovind.notes.ui.adapter.listener.OnSnapPositionChangeListener
import com.singhgeetgovind.notes.ui.adapter.listener.SnapOnScrollListener
import com.singhgeetgovind.notes.utils.avatar.Adventurer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BottomSheetDialogFragment(), OnSnapPositionChangeListener {
    @Inject lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentProfileBinding? = null
    private lateinit var profileAdapter : ProfileAdapter
    private lateinit var snapOnScrollListener:SnapOnScrollListener
    private var snapItemData : String? = null
    private lateinit var data : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        uploadFileLauncher_ = extracted()
        profileAdapter = ProfileAdapter(requireContext())
        profileAdapter.submit(Adventurer.values().toMutableList())
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
            if (!it?.trim().isNullOrBlank()) {
                binding.login.isEnabled = true
            }
        }

        binding.profileAdapter.apply{
            val snapHelper = PagerSnapHelper()
            this.attachToRecyclerView(snapHelper,SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL)
            this.adapter = profileAdapter
        }


        binding.login.setOnClickListener {
            sharedPreferences.saveSharedPrefData("FullName",
                binding.fullName.editText?.text?.trim().toString()
            )
            if(!this::data.isInitialized){
                sharedPreferences.saveSharedPrefData("ProfileImage",snapItemData)
            }
            val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.splashFragment,true)
            .build()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToListFragment(),navOptions)
        }
    }

    override fun onStop() {
        binding.profileAdapter.removeOnScrollListener(snapOnScrollListener)
        super.onStop()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        uploadFileLauncher_.unregister()
        _binding = null
    }

    override fun onSnapPositionChange(position: Int) {
        snapItemData = profileAdapter.currentList[position].getBASEURL()
    }
    private fun RecyclerView.attachToRecyclerView(
        snapHelper: PagerSnapHelper,
        notifyOnScroll: SnapOnScrollListener.Behavior
    ) {
        if(!this@ProfileFragment::snapOnScrollListener.isInitialized){
            snapOnScrollListener = SnapOnScrollListener(snapHelper, notifyOnScroll,this@ProfileFragment)
        }
        snapHelper.attachToRecyclerView(this)
        this.addOnScrollListener(snapOnScrollListener)
    }
}
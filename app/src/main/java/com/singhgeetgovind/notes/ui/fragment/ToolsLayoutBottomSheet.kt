package com.singhgeetgovind.notes.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singhgeetgovind.notes.databinding.ToolsLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ToolsLayoutBottomSheet:BottomSheetDialogFragment() {
    private lateinit var binding : ToolsLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ToolsLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.colorPaletteLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        return super.onCreateDialog(savedInstanceState)
    }
}
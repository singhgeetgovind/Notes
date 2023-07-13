package com.singhgeetgovind.notes.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.singhgeetgovind.notes.databinding.FragmentSearchDialogBinding
import com.singhgeetgovind.notes.ui.adapter.SearchAdapter
import com.singhgeetgovind.notes.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDialogFragment : DialogFragment(),SearchView.OnQueryTextListener {
    private val TAG = "SearchDialogFragment"
    private val searchAdapter: SearchAdapter by lazy { SearchAdapter() }
    private val searchViewModel : SearchViewModel by viewModels()
    private lateinit var binding : FragmentSearchDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            setGravity(Gravity.CENTER)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        with(binding){
            searchTopBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            searchField.setOnQueryTextListener(this@SearchDialogFragment)

        }
        searchViewModel.searchResult.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            searchAdapter.submitList(it)
        }
        binding.searchResult.adapter = searchAdapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrBlank()){
            Log.d(TAG, "onQueryTextChange: $query")
//            searchViewModel.searchQueryList(query)
            searchViewModel.searchQuery.value = query
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        return true
    }

}
package com.example.notes.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notes.model.Notes
import com.example.notes.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(private val repository: Repository) : ViewModel() {
    private val TAG = "SearchViewModel"
    var searchResult = MutableLiveData<List<Notes>>()
    private set

    fun searchQueryList(searchQuery: String) {
            searchResult.value = repository.searchQueryList(searchQuery).value
            Log.d(TAG, "searchQueryList: ${searchResult.value}")
    }
}
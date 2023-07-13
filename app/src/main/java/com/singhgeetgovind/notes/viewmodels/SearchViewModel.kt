package com.singhgeetgovind.notes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.singhgeetgovind.notes.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(private val repository: Repository) : ViewModel() {
    private val TAG = "SearchViewModel"
    /*var searchResult = MutableLiveData<List<Notes>>()
    private set*/

    var searchQuery = MutableLiveData<String>()
    private set

    var searchResult = Transformations.switchMap(searchQuery) { query ->
        repository.searchQueryList(query)
    }
    private set
}
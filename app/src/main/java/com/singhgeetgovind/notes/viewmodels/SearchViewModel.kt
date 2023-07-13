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

    val searchQuery = MutableLiveData<String>()

    val searchResult = Transformations.switchMap(searchQuery) { query ->
        repository.searchQueryList(query)
    }

    /*fun searchQueryList(searchQuery: String) {
            searchResult.value = repository.searchQueryList(searchQuery).value
            Log.d(TAG, "searchQueryList: ${searchResult.value}")
    }*/
}
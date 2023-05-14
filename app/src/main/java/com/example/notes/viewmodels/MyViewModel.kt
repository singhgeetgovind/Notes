package com.example.notes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.model.Notes
import com.example.notes.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
//    val apiData = repository.getApiData1()
    private val TAG = "MyViewModel"

    fun getData(): LiveData<List<Notes>> {
        return repository.getData()
    }

    fun addData(notes: Notes) {
        viewModelScope.launch {
            repository.addData(notes)
        }
    }

    fun deleteData(id: List<Int>) {
        viewModelScope.launch {
            repository.deleteData(id)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun updateData(notes: Notes) {
        viewModelScope.launch {
            repository.updateData(notes)
        }
    }


}

//class MyViewModelFactory @Inject constructor(private val repository: Repository) : ViewModelProvider.Factory{
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return MyViewModel(repository) as T
//    }
//
//}
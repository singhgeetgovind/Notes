package com.singhgeetgovind.notes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singhgeetgovind.notes.model.Notes
import com.singhgeetgovind.notes.repository.Repository
import com.singhgeetgovind.notes.utils.avatar.MicahAvatar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    var profileUrl: String = ""
    get() {
        profileUrl=field
        return field
    }
    set(value) {
        field = value.ifBlank {
            getLink()
        }
    }

    private fun getLink() : String{
        val name = MicahAvatar.values().random()
       return name.getBASEURL()
    }

    fun getData(): LiveData<List<Notes>> {
        return repository.getData()
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


}

//class MyViewModelFactory @Inject constructor(private val repository: Repository) : ViewModelProvider.Factory{
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return MyViewModel(repository) as T
//    }
//
//}
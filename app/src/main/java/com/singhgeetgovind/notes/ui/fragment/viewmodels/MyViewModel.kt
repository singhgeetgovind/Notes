package com.singhgeetgovind.notes.ui.fragment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singhgeetgovind.notes.data.model.Notes
import com.singhgeetgovind.notes.data.repository.Repository
import com.singhgeetgovind.notes.utils.avatar.Adventurer
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
    var searchQuery = MutableLiveData("")
        private set

    val list :LiveData<List<Notes>> = Transformations.switchMap(searchQuery) { query ->
            if(query.isNullOrBlank()){
                getData()
            } else{
                repository.searchQueryList(query)
            }
        }



    private fun getLink() : String{
        val name = Adventurer.values().random()
       return name.getBASEURL()
    }

    private fun getData(): LiveData<List<Notes>> {
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
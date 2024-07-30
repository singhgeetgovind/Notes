package com.singhgeetgovind.notes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singhgeetgovind.notes.model.Notes
import com.singhgeetgovind.notes.repository.Repository
import com.singhgeetgovind.notes.utils.NotesAction
import com.singhgeetgovind.notes.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class EditViewModel @Inject constructor(private val repository: Repository): ViewModel(){

    var intentId: Int = 0
    var date: Long? = null
        set(value) {
            if(value == null) {
                formattedDate = ""
            }
            field = value
        }

    var title: String = ""
        private set

    var description: String = ""
        private set

    private var id: Int = 0
    private val mutableNotesAction : MutableStateFlow<NotesAction> = MutableStateFlow(NotesAction.Add)
    val notesAction : StateFlow<NotesAction> = mutableNotesAction

    val listenValidation: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var formattedDate: String = ""
    private set

    private var millis : Long = System.currentTimeMillis()
    var calendar = Calendar.getInstance()
    var hour : Int = (TimeUnit.MILLISECONDS.toHours(millis) % 24).toInt()
    var minute : Int = (TimeUnit.MILLISECONDS.toMinutes(millis) % 60).toInt()

    fun setDateTime(): String{
        date = Utils.getInMilliSecond(
            calendar,
            hour,
            minute
        )
        formattedDate = Utils.formatDate(Utils.DATE_PATTERN,date)
        return formattedDate
    }

    fun setAction(n:NotesAction) {
        mutableNotesAction.value = n
        when(mutableNotesAction.value){
            NotesAction.Add -> Unit
            is NotesAction.Update<*>-> {
                ((mutableNotesAction.value as NotesAction.Update<*>).notes as Notes).also { n ->
                    title = n.title
                    description = n.description
                    id = n.id
                    date = n.eventDate
                    intentId = n.intentId!!
                    formattedDate = Utils.formatDate(Utils.DATE_PATTERN, n.eventDate)
                }
                listenValidation.value = true
            }
        }
    }

    fun setTitle(text : String) {
        title = text
        fieldValidation()
    }
    fun setDescription(text : String){
        description = text
        fieldValidation()
    }
    private fun fieldValidation(){

        if(description.isBlank()){
            listenValidation.value = false
            return
        }
        if(title.isBlank()){
            listenValidation.value = false
            return
        }
        listenValidation.value = true

    }
    fun getData(): LiveData<List<Notes>> {
        return repository.getData()
    }

    private fun addData(notes: Notes) {
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

    private fun updateData(notes: Notes) {
        viewModelScope.launch {
            repository.updateData(notes)
        }
    }
    fun saveData(){
        intentId = if(date == null ) 0 else System.currentTimeMillis().toString().substring(5,11).toInt()
        val notes = Notes(
            id = id,
            title = title,
            description = description,
            hasPriority = -1,
            createdDate = Date(System.currentTimeMillis()),
            eventDate = date,
            intentId = intentId,
        )
        when(notesAction.value){
            NotesAction.Add -> {
                addData(notes)
            }
            is NotesAction.Update<*> -> {
                updateData(notes)
            }
        }
    }
}

package com.singhgeetgovind.notes.utils

sealed class NotesAction {
    object Add : NotesAction()
    data class Update <T> (val notes: T? = null) : NotesAction()
}
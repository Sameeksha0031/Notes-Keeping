package com.example.noteskeeping.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteskeeping.model.AuthListener
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes

class NotesViewModel(private var noteServices: NoteServices) : ViewModel(){
    var _NewNotes = MutableLiveData<AuthListener>()
    var newNotes = _NewNotes as LiveData<AuthListener>

    fun createNewNote(notes: Notes){
        noteServices.writeNotes(notes){
            if(it.status){
                _NewNotes.value = it
            }
        }
    }
}
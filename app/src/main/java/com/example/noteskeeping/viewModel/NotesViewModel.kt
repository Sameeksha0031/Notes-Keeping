package com.example.noteskeeping.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteskeeping.model.*

class NotesViewModel(private var noteServices: NoteServices) : ViewModel(){
    var _NewNotes = MutableLiveData<AuthListener>()
    var _ReadNote = MutableLiveData<NotesAuthListener>()
    var _DeleteNote = MutableLiveData<AuthListener>()
    var _ReadSingleNote = MutableLiveData<EditNoteAuthListener>()
    var _UpdateSingleNote = MutableLiveData<AuthListener>()
    var newNotes = _NewNotes as LiveData<AuthListener>
    var readnote = _ReadNote as LiveData<NotesAuthListener>
    var deleteNote = _DeleteNote as LiveData<AuthListener>
    var readSingleNote = _ReadSingleNote as LiveData<EditNoteAuthListener>
    var updateSingleNote = _UpdateSingleNote as LiveData<AuthListener>

    fun createNewNote(notes: Notes){
        noteServices.writeNotes(notes){
            if(it.status){
                _NewNotes.value = it
            }
        }
    }

    fun getNotes(){
         noteServices.readNotes(){
             if(it.status){
                 _ReadNote.value = it
             }
         }
    }

    fun deleteNote(noteId : String){
        noteServices.deleteNote(noteId){
            if(it.status){
                 _DeleteNote.value = it
            }
        }
    }

    fun readSingleNote(noteId : String){
        noteServices.readSingleNote(noteId){
            if(it.status){
                _ReadSingleNote.value = it
            }
        }
    }

    fun updateSingleNote(note : Notes ,noteId: String){
        noteServices.updateSingleNote(note,noteId){
            if(it.status){
                _UpdateSingleNote.value = it
            }
        }
    }
}
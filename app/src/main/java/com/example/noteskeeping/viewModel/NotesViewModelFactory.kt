package com.example.noteskeeping.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.UserAuthServices

class NotesViewModelFactory(private  val notesServices: NoteServices) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(notesServices) as T
    }
}
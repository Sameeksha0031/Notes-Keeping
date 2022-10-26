package com.example.noteskeeping.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.database.DataBaseHelper
import com.example.noteskeeping.databinding.FragmentArchieveNoteBinding
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.viewModel.NotesViewModel

class ArchiveNoteFragment : Fragment() {
    lateinit var binding: FragmentArchieveNoteBinding
    lateinit var notesViewModel: NotesViewModel
    lateinit var recyclerView: RecyclerView
    lateinit  var noteId : String
    //var bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArchieveNoteBinding.bind(requireView())
        notesViewModel = NotesViewModel(NoteServices(DataBaseHelper(requireContext())))
        return inflater.inflate(R.layout.fragment_archieve_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.get("noteId") as String
        var makeNote_Archeive  = arguments?.get("makeNote_archeive")

        recyclerView = binding.archeiveRecyclerView
        recyclerView.setHasFixedSize(true)
    }

    fun settingNoteAsArchive(){
        notesViewModel.readSingleNote(noteId)
        notesViewModel.readSingleNote.observe(viewLifecycleOwner , Observer {
            if(it.status){
                var title :  String = it.notes.title
                var noteContent : String = it.notes.notes
                var note = Notes(noteContent,noteId,title)
                //recyclerView.adapter = ArchiveNoteViewAdapter(it.note)
            }
        })
    }
}
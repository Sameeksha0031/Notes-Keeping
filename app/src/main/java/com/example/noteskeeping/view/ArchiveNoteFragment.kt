package com.example.noteskeeping.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var archiveList : ArrayList<Notes>
    lateinit var noteList: ArrayList<Notes>
    lateinit  var noteId : String
    var note = Notes()
    var currentStateOfNote : Int = 0
    val archiveNote : Int = 0
    val noteIsNotArchive : Int = 1
    //var bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_archieve_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArchieveNoteBinding.bind(view)
        archiveList = ArrayList<Notes>()
        noteList = ArrayList<Notes>()
        recyclerView = binding.archeiveRecyclerView
        recyclerView.setHasFixedSize(true)
        notesViewModel = NotesViewModel(NoteServices(DataBaseHelper(requireContext())))
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val isArchive = arguments?.get("add_note_to_archive")
        if (isArchive != null && isArchive == 2) {
            addingNotesToArchive()
        }
    }

    fun addingNotesToArchive() {
        notesViewModel.getNotes()
        notesViewModel.readNote.observe(viewLifecycleOwner, Observer {
            if(it.status){
                noteList = it.noteArrayList
                for(notes in noteList){
                    if(note.isArchive == true){
                        archiveList.add(notes)
                        Log.d("ArchiveFragment","note List = $archiveList.size")
                    }
                }
//                recyclerView.adapter = NoteRecyclerViewAdapter(archiveList)
            }
        })
        recyclerView.adapter = NoteRecyclerViewAdapter(archiveList)
    }
//        val noteId = arguments?.getString("noteId")!!.toString()
//        var note: Notes
//        if(currentStateOfNote == archiveNote) {
//            notesViewModel.readSingleNote(noteId)
//            notesViewModel.readSingleNote.observe(viewLifecycleOwner, Observer {
//                if (it.status) {
//                    note = Notes(
//                        notes = it.notes.notes,
//                            noteId = it.notes.noteId,
//                            title = it.notes.title,
//                            isArchive = true
//                        )
//
//                        notesViewModel.updateSingleNote(note, noteId)
//                        notesViewModel.updateSingleNote.observe(viewLifecycleOwner, Observer {
//                            if (it.status) {
//                                // Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
//
//                                archiveList.add(note)
//                                Log.d("ArchiveNote","${archiveList.size}")
//                                //Toast.makeText(context, "Note add to Archive", Toast.LENGTH_SHORT)
//                                    //.show()
//                                currentStateOfNote = noteIsNotArchive
//                                recyclerView.adapter = NoteRecyclerViewAdapter(archiveList)
////                                notesViewModel.getNotes()
////                                notesViewModel.readNote.observe(viewLifecycleOwner, Observer {
////                                    if(it.status){
////                                        noteList = it.noteArrayList
////                                        recyclerView.adapter = NoteRecyclerViewAdapter(noteList)
////                                    }
////                                })
//                            }
//                        })
//                    }
//                    notesViewModel.getNotes()
//                    notesViewModel.readNote.observe(viewLifecycleOwner, Observer {
//                        if(it.status){
//                            noteList = it.noteArrayList
//                            recyclerView.adapter = NoteRecyclerViewAdapter(noteList)
//                        }
//                    })
//
//                })
//            }
//            if(currentStateOfNote == noteIsNotArchive) {
//                notesViewModel.readSingleNote(noteId)
//                notesViewModel.readSingleNote.observe(viewLifecycleOwner, Observer {
//                    if (it.status) {
//                        note = Notes(
//                            notes = it.notes.notes,
//                            noteId = it.notes.noteId,
//                            title = it.notes.title,
//                            isArchive = false
//                        )
//
//                        notesViewModel.updateSingleNote(note, noteId)
//                        notesViewModel.updateSingleNote.observe(viewLifecycleOwner, Observer {
//                            if (it.status) {
//                                // Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
//                                archiveList.add(note)
//                                Toast.makeText(context, "Note add to Archive", Toast.LENGTH_SHORT)
//                                    .show()
//                                currentStateOfNote = archiveNote
//                                recyclerView.adapter = NoteRecyclerViewAdapter(archiveList)
//                                //displayAllNotesInRecyclerView()
//                            }
//                        })
//                    }
//
//                })
//            }
//        }
//
}

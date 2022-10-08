package com.example.noteskeeping.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentNoteBinding
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.viewModel.NotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteFragment : Fragment() {
    lateinit var binding : FragmentNoteBinding
    private lateinit var recyclerView: RecyclerView
    lateinit var floatingActionButton: FloatingActionButton
    var recyclerViewManager: RecyclerView.LayoutManager?= null
    private var noteArrayList = ArrayList<Notes>()
    var noteAdapter : RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>?=null
    var notesViewModel = NotesViewModel(NoteServices())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding =  FragmentNoteBinding.bind(view)
        floatingActionButton = FloatingActionButton(requireContext())
        //recyclerView = binding.notesList

        recyclerViewManager = LinearLayoutManager(requireContext())

        binding.notesList.layoutManager = recyclerViewManager

        noteAdapter = NoteRecyclerViewAdapter(noteArrayList)

        binding.notesList.adapter = noteAdapter

        notesViewModel.getNotes(noteArrayList)
        notesViewModel.readnote.observe(viewLifecycleOwner, Observer {
            if(it.status){
                Toast.makeText(context,"implementation successful",Toast.LENGTH_SHORT).show()
            }
        })

        floatingActionButton = binding.floatingButton
        floatingActionButton.setOnClickListener {
            Toast.makeText(context, "Floating button is click", Toast.LENGTH_SHORT).show()
            val fragment = HomeFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.home_activity_fragment_container, fragment)?.commit()
        }
    }
}
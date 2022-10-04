package com.example.noteskeeping.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentHomeBinding
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.model.UserAuthServices
import com.example.noteskeeping.viewModel.NotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var auth: FirebaseAuth
    lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        notesViewModel = NotesViewModel(NoteServices())


        binding.signOutButton.setOnClickListener {
            auth.signOut()
            Log.d(TAG, "Sign Out")
            var intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }


        binding.saveButton.setOnClickListener {
            val noteTitle = binding.title.text.toString().trim()
            val newNote = binding.notes.text.toString().trim()
            val notes = Notes(noteId = "", title = noteTitle, notes = newNote)

            notesViewModel.createNewNote(notes)
            notesViewModel.newNotes.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Error......", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}
package com.example.noteskeeping.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentHomeBinding
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.viewModel.NotesViewModel
import com.google.firebase.auth.FirebaseAuth


class CreatingNotesFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var auth: FirebaseAuth
    lateinit var notesViewModel: NotesViewModel
    val bundle = Bundle()

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


//        binding.signOutButton.setOnClickListener {
//            auth.signOut()
//            Log.d(TAG, "Sign Out")
//            var intent = Intent(requireContext(), MainActivity::class.java)
//            startActivity(intent)
//        }

        var noteId_getFrom_Adapter = arguments?.getString("noteId")
        var edit_Operation = arguments?.getInt("edit_note")

        if(edit_Operation != null && edit_Operation == 0){
            displayNote(noteId_getFrom_Adapter)
        }


        binding.updateNote.setOnClickListener{
//            bundle.putString("noteId",noteId_getFrom_Adapter)
//            bundle.putInt("edit_Operation", edit_Operation!!)
//            fragment.arguments = bundle
            updateNote(noteId_getFrom_Adapter)
            val fragment = NoteFragment()
            val transaction = it.context as AppCompatActivity
            transaction.supportFragmentManager.beginTransaction()
                .replace(R.id.home_activity_fragment_container, fragment)
                .addToBackStack(null)
                .commit()
         }

        binding.saveButton.setOnClickListener {
            val noteTitle = binding.title.text.toString().trim()
            val newNote = binding.notes.text.toString().trim()
            val notes = Notes(noteId = "", title = noteTitle, notes = newNote)

            notesViewModel.createNewNote(notes)
            notesViewModel.newNotes.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                    val fragment = NoteFragment()
                    val transaction = context as AppCompatActivity
                    transaction.supportFragmentManager.beginTransaction()
                        .replace(R.id.home_activity_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }else{
                    Toast.makeText(context, "Error......", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    fun displayNote(noteId_getFrom_Adapter: String?) {
        if (noteId_getFrom_Adapter != null) {
            notesViewModel.readSingleNote(noteId_getFrom_Adapter)
            notesViewModel.readSingleNote.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    var title : TextView = binding.title
                     title.text = it.notes.title
                    var noteContent : TextView = binding.notes
                    noteContent.text = it.notes.notes
                    //Log.d("CreatingNotes","$")
                }
            })
        }
    }

    fun updateNote(noteId_getFrom_Adapter: String?){
        val noteTitle = binding.title.text.toString().trim()
        val newNote = binding.notes.text.toString().trim()
        val note = Notes(noteId = noteId_getFrom_Adapter.toString(), title = noteTitle, notes = newNote)

        if (noteId_getFrom_Adapter != null) {
            notesViewModel.updateSingleNote(note,noteId_getFrom_Adapter)
            notesViewModel.updateSingleNote.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
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
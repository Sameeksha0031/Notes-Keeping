package com.example.noteskeeping.model

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.view.NoteRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NoteServices() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFireStore: FirebaseFirestore
    private var firebaseStore: FirebaseStorage? = null

    init {
        noteInitServices()
    }

    private fun noteInitServices() {
        auth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()
        firebaseStore = FirebaseStorage.getInstance()
    }

    fun writeNotes(note: Notes, listener: (AuthListener) -> Unit) {                     ////UUID.randomUUID().toString()
        val userID = auth.currentUser?.uid
        val noteID = UUID.randomUUID().toString()
        val noteHashMap = HashMap<String, String>()
        if (userID != null) {
            noteHashMap["NoteID"] = noteID
            noteHashMap["Title"] = note.title
            noteHashMap["Note"] = note.notes
            firebaseFireStore.collection("users").document(userID).collection("Notes")
                .document(noteID)
                .set(noteHashMap)
                .addOnSuccessListener {
                    listener(AuthListener(true, "note update"))
                }
        }
    }

    fun readNotes(listener: (NotesAuthListener) -> Unit){
        val userID = auth.currentUser?.uid
        var notesList = ArrayList<Notes>()
        if(userID != null){
            firebaseFireStore.collection("users").document(userID)
                .collection("Notes")
                .get()
                .addOnSuccessListener {
                    if(it != null){
                        for(doc in it.documents){
                            var noteContent : String = doc["Note"].toString()
                            var noteId : String = doc["NoteID"].toString()
                            var noteTitle : String = doc["Title"].toString()
                            var notes = Notes(notes =  noteContent, noteId =  noteId, title = noteTitle)
                            notesList.add(notes!!)
                        }
                        Log.d("NoteService","${notesList.size.toString()}" )

                        for(note in notesList){

                            Log.d("NoteService","Title is ${note.title}")
                        }
                    }
                    listener(NotesAuthListener(notesList,true,"Data added successfully"))
                }
        }
    }
}
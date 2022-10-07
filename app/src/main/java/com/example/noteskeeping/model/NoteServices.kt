package com.example.noteskeeping.model

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.view.NoteRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NoteServices() {
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteRecyclerViewAdapter: NoteRecyclerViewAdapter
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

    fun readNotes(noteList: ArrayList<Notes>,listener: (AuthListener) -> Unit){
        val userID = auth.currentUser?.uid
        noteRecyclerViewAdapter = NoteRecyclerViewAdapter(noteList)
        if(userID != null){
            firebaseFireStore.collection("users").document(userID)
                .collection("Notes")
                .addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {

                        if(error != null){
                            Log.d("Firestore Error",error.message.toString())
                            return
                        }
                        for(dc : DocumentChange in value?.documentChanges!!){

                            if(dc.type == DocumentChange.Type.ADDED){
                                noteList.add(dc.document.toObject(Notes::class.java))
                            }
                        }

                        noteRecyclerViewAdapter.notifyDataSetChanged()
                    }
                })
        }
    }
}
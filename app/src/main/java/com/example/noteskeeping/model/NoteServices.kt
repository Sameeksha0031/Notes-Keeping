package com.example.noteskeeping.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
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
}
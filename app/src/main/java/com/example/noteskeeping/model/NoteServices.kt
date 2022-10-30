package com.example.noteskeeping.model

import android.util.Log
import com.example.noteskeeping.database.DataBaseHelper
import com.example.noteskeeping.utility.NetworkConnectivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NoteServices(var dataBaseHelper: DataBaseHelper) {
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
        // val noteID = UUID.randomUUID().toString()
        val noteHashMap = HashMap<String, String>()
        if (userID != null) {
           // noteHashMap["NoteID"] = note.noteId
            noteHashMap["Title"] = note.title
            noteHashMap["Note"] = note.notes
            noteHashMap["ArchiveStatus"] = note.isArchive.toString()
            var documentReference = firebaseFireStore.collection("users").document(userID).collection("Notes")
                    .document()
            note.noteId = documentReference.id
            noteHashMap["NoteID"] = note.noteId
                documentReference.set(noteHashMap).addOnSuccessListener {

                            var noteId = note.noteId
                            firebaseFireStore.collection("users").document(userID).collection("Notes")
                                .document(noteId).set(noteHashMap)
                            dataBaseHelper.addNotes(note,noteId)
                            listener(AuthListener(true, "note added successfully"))

                    }
        }
    }

    fun readNotes(listener: (SearchAuthListener) -> Unit) {
        val userID = auth.currentUser?.uid
        var notesList = ArrayList<Notes>()
        var searchNoteList = ArrayList<String>()
        if (userID != null) {
            firebaseFireStore.collection("users").document(userID)
                .collection("Notes")
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        for (doc in it.documents) {
                            var noteContent: String = doc["Note"].toString()
                            var noteId: String = doc["NoteID"].toString()
                            var noteTitle: String = doc["Title"].toString()
                            var archive : String = doc["ArchiveStatus"].toString()
                            var notes =
                                Notes(notes = noteContent, noteId = noteId, title = noteTitle, isArchive = archive.toBoolean())
                            notesList.add(notes!!)
                        }
                        Log.d("NoteService", "${notesList.size.toString()}")

                        for (note in notesList) {

                            Log.d("NoteService", "Title is ${note.title}")
                        }
                    }
                    //dataBaseHelper.getALLNotes()
                    listener(SearchAuthListener(notesList,searchNoteList, true, "Getting Notes.... "))
                }
        }
    }

    fun deleteNote(noteId: String, listener: (AuthListener) -> Unit) {
        val userID = auth.currentUser?.uid
        if (userID != null) {
            firebaseFireStore.collection("users").document(userID)
                .collection("Notes").document(noteId).delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                       // dataBaseHelper.deleteUser(noteId)
                        listener(AuthListener(true, "User deleted successfully"))
                    }
                }
        }
    }

    fun readSingleNote(noteId: String, listener: (EditNoteAuthListener) -> Unit) {
        val userID = auth.currentUser?.uid.toString()
        if (userID != null) {
            firebaseFireStore.collection("users").document(userID)
                .collection("Notes").document(noteId).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        var note = Notes(
                            notes = it.result.getString("Note").toString(),
                            noteId = it.result.getString("NoteID").toString(),
                            title = it.result.getString("Title").toString(),
                            isArchive = it.result.getString("ArchiveStatus").toBoolean()
                        )
                       // dataBaseHelper.getSingleNote(noteId)
                        listener(EditNoteAuthListener(note, true, "success"))
                    }
                }
        }

    }

    fun updateSingleNote(note: Notes, noteId: String, listener: (AuthListener) -> Unit) {
        val userID = auth.currentUser?.uid.toString()
        val noteHashMap = HashMap<String, String>()
        noteHashMap["Note"] = note.notes
        noteHashMap["NoteID"] = note.noteId
        noteHashMap["Title"] = note.title
        noteHashMap["Archive Status"] = note.isArchive.toString()
        firebaseFireStore.collection("users").document(userID).collection("Notes")
            .document(noteId)
            .set(noteHashMap)
            .addOnSuccessListener {
                //dataBaseHelper.updateTask(note, noteId)
                listener(AuthListener(true, "note update"))
            }
    }

}


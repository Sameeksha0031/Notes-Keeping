package com.example.noteskeeping.api

import com.example.noteskeeping.model.Notes
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// BASE_URL ="https://firestore.googleapis.com/v1beta1/"
interface NoteServicesInterface{
    @GET("./projects/noteskeeping-a3ed9/databases/(default)/documents/users/{UserId}/Notes")
    fun getNoteList(): Call<ArrayList<Notes>>
}
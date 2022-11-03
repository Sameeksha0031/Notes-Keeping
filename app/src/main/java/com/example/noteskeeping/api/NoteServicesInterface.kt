package com.example.noteskeeping.api

import com.example.noteskeeping.model.Notes
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL ="https://firestore.googleapis.com/v1beta1/"
interface NoteServicesInterface{
    @GET("projects/noteskeeping-a3ed9/databases/(default)/documents/users/{UserId}/Notes?key=AIzaSyDRrtzHAgisCfgMlui5YxEc85_55z5e5Cs")
    fun getNoteList(): Call<ArrayList<Notes>>
}
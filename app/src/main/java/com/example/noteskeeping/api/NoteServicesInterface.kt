package com.example.noteskeeping.api

import com.example.noteskeeping.model.RetrofitRetriver
import com.example.noteskeeping.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL ="https://firestore.googleapis.com/v1beta1/"
interface NoteServicesInterface{
   // @GET("./projects/noteskeeping-a3ed9/databases/(default)/documents/users/{UserId}/Notes?key=AIzaSyDRrtzHAgisCfgMlui5YxEc85_55z5e5Cs")
    //fun getNoteList(): Call<RetrofitRetriver>

    @GET("./projects/noteskeeping-a3ed9/databases/(default)/documents/users/5y8WLDMNPDN0nZQM6JHmHDbb3SM2/Notes?key=AIzaSyDRrtzHAgisCfgMlui5YxEc85_55z5e5Cs")
    //@GET("users/Notes?key=AIzaSyDRrtzHAgisCfgMlui5YxEc85_55z5e5Cs")
    fun getNoteList(): Call<RetrofitRetriver>
}
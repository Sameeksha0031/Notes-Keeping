package com.example.noteskeeping.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    var okHttpClient : OkHttpClient ?= null
    var retrofitClient : RetrofitClient ?= null
    private lateinit var api : NoteServicesInterface

    init {
        val httpClient = OkHttpClient.Builder()
        okHttpClient = httpClient.connectTimeout(100,TimeUnit.SECONDS).readTimeout(100,TimeUnit.SECONDS)
            .writeTimeout(100,TimeUnit.SECONDS).build()
    }
    fun getInstance():RetrofitClient ?{
        if(retrofitClient == null){
            retrofitClient = RetrofitClient
        }
        return retrofitClient
    }
    fun getMyApi() : NoteServicesInterface{
        api = Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(NoteServicesInterface::class.java)
        return api
    }

}
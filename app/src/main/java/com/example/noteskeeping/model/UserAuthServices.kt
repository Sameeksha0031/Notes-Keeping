package com.example.noteskeeping.model

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class UserAuthServices(){
    private lateinit var auth: FirebaseAuth

    init {
        initService()
    }

    private fun initService(){
        auth = FirebaseAuth.getInstance()
    }

    fun userRegistration(user: User,listener: (AuthListener)-> Unit){
        auth!!.createUserWithEmailAndPassword(
           user.email,user.password
        ).addOnCompleteListener() {
            if (it.isSuccessful) {
                Log.d(ContentValues.TAG,"Create user successfully")
                listener(AuthListener(true,"user registration successful"))
            } else {
                Log.d(ContentValues.TAG,"Create user fail")
                listener(AuthListener(true,"user registration Failed"))
            }
        }
    }
}

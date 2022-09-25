package com.example.noteskeeping.model

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.noteskeeping.view.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

    fun userLogin(user: User,listener: (AuthListener) -> Unit) {
        auth!!.signInWithEmailAndPassword(user.email,user.password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val userLogin = auth.currentUser
                var updateUser = updateUI(userLogin)
                if(updateUser == true){
                    Log.d(ContentValues.TAG,"User Login successfully")
                    listener(AuthListener(true,"Login Successful"))
                }else{
                    Log.d(ContentValues.TAG,"User Login fail")
                    listener(AuthListener(true,"Login Failed"))
                }

            }else{
                Log.d(TAG,"Authentication : Fail",task.exception)
                updateUI(null)
            }

        }
    }
    fun updateUI(currentUser: FirebaseUser?) : Boolean{
        var emailToverify : Boolean = false
        if(currentUser != null){
            var emailToverify = verifyEmail()
            return emailToverify
        }
        return emailToverify
    }
    fun verifyEmail() : Boolean{
        val user = auth.currentUser
        val vemail : Boolean? = user?.isEmailVerified
        //val intent = Intent(context, HomeActivity::class.java)
        //startActivity(intent)
        if(vemail!!){
            return true
        }else{
            return false
            auth.signOut()
        }
    }
    fun checkingForUser(listener: (AuthListener) -> Unit){
        var currentUser : FirebaseUser? = auth.currentUser
        if(currentUser != null){
            listener(AuthListener(true,""))
        }
    }
}

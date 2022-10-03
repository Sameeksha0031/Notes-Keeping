package com.example.noteskeeping.model

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.HashMap

class UserAuthServices() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFireStore: FirebaseFirestore
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    val userMapStore = HashMap<String, String>()

    init {
        initService()
    }

    private fun initService() {
        auth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()
        firebaseStore = FirebaseStorage.getInstance()
    }

    fun userRegistration(user: User, listener: (AuthListener) -> Unit) {
        auth!!.createUserWithEmailAndPassword(
            user.email, user.password
        ).addOnCompleteListener() {
            if (it.isSuccessful) {
                Log.d(ContentValues.TAG, "Create user successfully")
                listener(AuthListener(true, "user registration successful"))
                saveFireStore(user)
            } else {
                Log.d(ContentValues.TAG, "Create user fail")
                listener(AuthListener(true, "user registration Failed"))
            }
        }
    }

    fun userLogin(user: User, listener: (AuthListener) -> Unit) {
        auth!!.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userLogin = auth.currentUser
                var updateUser = updateUI(userLogin)
                //if(updateUser == true){
                Log.d(ContentValues.TAG, "User Login successfully")
                listener(AuthListener(true, "Login Successful"))
                //}else{
                //  Log.d(ContentValues.TAG,"User Login fail")
                //listener(AuthListener(false,"Login Failed"))
                //}

            } else {
                Log.d(TAG, "Authentication : Fail", task.exception)
                listener(AuthListener(false, "Login Failed"))
                updateUI(null)
            }

        }
    }

    fun updateUI(currentUser: FirebaseUser?): Boolean {
        var emailToverify: Boolean = false
        if (currentUser != null) {
            emailToverify = true
            return emailToverify
        }
        return emailToverify
    }


    fun checkingForUserStatus(listener: (AuthListener) -> Unit) {
        var currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            listener(AuthListener(true, ""))
        }
    }

    fun userForgotPassword(user: User, listener: (AuthListener) -> Unit) {
        auth!!.sendPasswordResetEmail(user.email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener(AuthListener(true, "Email is sent"))
                    Log.d(TAG, "Email sent")
                }else{
                    listener(AuthListener(true, "Email is not sent"))
                }
            }
    }

    fun saveFireStore(user: User) {
        val userID = auth.currentUser?.uid.toString()
        //val userMapStore = HashMap<String, String>()
        userMapStore["UserId"] = userID
        userMapStore["UserName"] = user.userName
        userMapStore["UserEmail"] = user.email
        userMapStore["Password"] = user.password
        userMapStore["Profile"] = user.profile

        firebaseFireStore.collection("users").document(userID)
            .set(userMapStore).addOnSuccessListener {
            }
    }

     fun readFireStore(user : User,dialogAuthListener: (DialogAuthListener)-> Unit)  {
        val userID = auth.currentUser?.uid
        lateinit var userInformation : User
        if (userID != null) {
            firebaseFireStore.collection("users").document(userID)
                .get()
                .addOnCompleteListener(OnCompleteListener {
                    if(it.isSuccessful){
                        userInformation = User(userId = it.result.getString("UserId").toString(),
                            userName = it.result.getString("UserName").toString(), email =
                            it.result.getString("UserEmail").toString(), password =
                            it.result.getString("Password").toString(),it.result.getString("Profile").toString())
                        dialogAuthListener(DialogAuthListener(userInformation,"Displaying the information", true))
                    }
                })
        };
    }

    fun uploadImage(user: User,filePath : Uri , listener: (AuthListener) -> Unit){
        val userID = auth.currentUser?.uid
        //val userMapStore = HashMap<String, String>()
        if(userID != null) {
            if (filePath != null) {
                val storageRef = FirebaseStorage.getInstance().getReference("myImages/"+ userID.toString()).child("profile")
                val uploadTask = storageRef?.putFile(filePath!!)
                uploadTask?.addOnSuccessListener {
                    val downloadUrl = storageRef.downloadUrl
                    downloadUrl.addOnSuccessListener {
                        user.profile = it.toString()
                        listener(AuthListener(true, "Image Uploaded successfully "))
                    }
                }
            } else {
                listener(AuthListener(false, "Image Failed to Upload"))
            }
        }
    }

    fun writeNotes(note : Notes , listener: (AuthListener) -> Unit){
        val userID = auth.currentUser?.uid
        val noteHashMap = HashMap<String,String>()
        if(userID != null){
            noteHashMap["NoteID"] = note.noteId
            noteHashMap["Title"] = note.title
            noteHashMap["Note"] = note.notes
        }
        firebaseFireStore.collection("users").document().collection("Notes").document("NotesID")
            .get()
            .addOnSuccessListener {  }
    }
}

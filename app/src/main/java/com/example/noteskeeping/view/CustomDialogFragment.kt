package com.example.noteskeeping.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentDialogBinding
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices
import com.example.noteskeeping.viewModel.CustomDialogModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class CustomDialogFragment : DialogFragment() {
    lateinit var binding: FragmentDialogBinding
    lateinit var customDialogModel: CustomDialogModel
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFireStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBinding.inflate(inflater,container,false)
        customDialogModel = CustomDialogModel(UserAuthServices())
        //var email = binding.retriveEmail
        //var name = binding.retriveName
        auth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()

        readFireStore()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun readFireStore(){
        var userName = ""
        var userEmail = ""
        var userPassword = ""
        val userID = auth.currentUser?.uid.toString()
        firebaseFireStore.collection("users").document(userID).get().addOnSuccessListener{
            if(it.exists()){
                userName = it.get("UserName") as String
                userEmail = it.get("UserEmail") as String
                val user = User(userID ,userName = userName, email = userEmail, password = userPassword)
                binding.retriveEmail.text = user.email.toString()
            }
        }
    }
}
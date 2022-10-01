package com.example.noteskeeping.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.noteskeeping.databinding.FragmentDialogBinding
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices
import com.example.noteskeeping.viewModel.CustomDialogModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.reflect.Array.get

class CustomDialogFragment : DialogFragment() {
    lateinit var binding: FragmentDialogBinding
    lateinit var customDialogModel: CustomDialogModel
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFireStore: FirebaseFirestore
    private lateinit var documentReference: DocumentReference
    lateinit var uid : String
    //private lateinit var user: User
    lateinit var email  : TextView
    lateinit var name : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBinding.inflate(inflater,container,false)
        customDialogModel = CustomDialogModel(UserAuthServices())
        auth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()

        //readFireStore()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customDialogModel.viewProfile(User(userId = "", userName = "",email="", password = ""))
        customDialogModel.profileView.observe(viewLifecycleOwner, Observer {
            if(it.status){
                binding.retriveEmail.text = it.user.email
                Toast.makeText(requireContext(),it.msg,Toast.LENGTH_LONG).show()
            }
        })

    }

    /*private fun readFireStore(){
        val userID = auth.currentUser?.uid

        if (userID != null) {
            firebaseFireStore.collection("users").document(userID)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        //binding.retriveEmail.setText(it.result.getString("UserEmail")).toString()
                        //binding.retriveName.setText(it.result.getString("UserName")).toString()
                        binding.retriveEmail.text = it.result.getString("UserEmail").toString()
                        email = binding.retriveEmail
                        //binding.retriveName.text = it.result.getString("UserName")
                        Toast.makeText(requireContext(),"${email}",Toast.LENGTH_LONG).show()
                    }
                }
        };
    }*/

}
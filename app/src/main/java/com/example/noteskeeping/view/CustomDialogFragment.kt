package com.example.noteskeeping.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentDialogBinding
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices
import com.example.noteskeeping.viewModel.CustomDialogModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.reflect.Array.get

class CustomDialogFragment : DialogFragment() {
    lateinit var binding: FragmentDialogBinding
    lateinit var customDialogModel: CustomDialogModel
    lateinit var floatingActionButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBinding.inflate(inflater,container,false)
        customDialogModel = CustomDialogModel(UserAuthServices())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customDialogModel.viewProfile(
            User(
                userId = "",
                userName = "",
                email = "",
                password = "",
                profile = ""
            )
        )
        customDialogModel.profileView.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                binding.retriveEmail.text = it.user.email
                binding.retriveName.text = it.user.userName
                Toast.makeText(requireContext(), it.msg, Toast.LENGTH_LONG).show()
            }
        })

        binding.changeProfile.setOnClickListener{
            val fragment = ProfileFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction!!.replace(R.id.home_activity_fragment_container, fragment)
            transaction.commit()
            Toast.makeText(requireContext(),"Floating Button", Toast.LENGTH_LONG).show()
        }
    }
}
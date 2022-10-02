package com.example.noteskeeping.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentSignUpBinding
import com.example.noteskeeping.model.AuthListener
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices
import com.example.noteskeeping.viewModel.RegisterViewModel
import com.example.noteskeeping.viewModel.RegisterViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        registerViewModel = ViewModelProvider(this,RegisterViewModelFactory(UserAuthServices())).get(RegisterViewModel::class.java)
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnBack.setOnClickListener {
            auth.signOut()
            val fragment = LogInFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }
        binding.signInBtnsign.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        var userName = ""
        var userEmail = ""
        var userPassword = ""

        userName = binding.signEditUsername.text.toString().trim()
        userEmail = binding.signInEditEmail.text.toString().trim()
        userPassword = binding.signInEditPassword.text.toString().trim()
        var userConfirm = binding.signInConfirmpw.text.toString().trim()

        val user = User(userName = userName, email = userEmail, password = userPassword, profile = "")

        if (userName.isEmpty()) {
            binding.signEditUsername.error = "Please enter the User Name"
            binding.signEditUsername.requestFocus()
            return
        }
        if (userEmail.isEmpty()) {
            binding.signInEditEmail.error = "Please enter the Email Address"
            binding.signInEditEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.toString())
                .matches()
        ) {
            binding.signInEditEmail.error = "Please enter valid Email Address"
            binding.signInEditEmail.requestFocus()
            return
        }

        if (userPassword.isEmpty()) {
            binding.signInEditPassword.error = "Please enter the password"
            binding.signInEditPassword.requestFocus()
            return
        }
        if (userConfirm.isEmpty() || binding.signInConfirmpw.text == binding.signInEditPassword.text ) {
            binding.signInEditPassword.error = "Please cofirm the password"
            binding.signInEditPassword.requestFocus()
            return
        }

        registerViewModel.registerUser(user)
        registerViewModel.userRegisterStatus.observe(viewLifecycleOwner, Observer{
            if(it.status){
                Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
            }
        })

    }

}
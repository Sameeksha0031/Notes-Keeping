package com.example.noteskeeping

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteskeeping.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth




class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        binding.btnBack.setOnClickListener {
            auth.signOut()
            var fragment = LogInFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }
        binding.signInBtnsign.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        if (binding.signEditUsername.text.isEmpty()) {
            binding.signEditUsername.error = "Please enter the User Name"
            binding.signEditUsername.requestFocus()
            return
        }
        if (binding.signInEditEmail.text.isEmpty()) {
            binding.signInEditEmail.error = "Please enter the Email Address"
            binding.signInEditEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.signInEditEmail.text.toString())
                .matches()
        ) {
            binding.signInEditEmail.error = "Please enter valid Email Address"
            binding.signInEditEmail.requestFocus()
            return
        }

        if (binding.signInEditPassword.text.toString().isEmpty()) {
            binding.signInEditPassword.error = "Please enter the password"
            binding.signInEditPassword.requestFocus()
            return
        }
        if (binding.signInConfirmpw.text.toString().isEmpty() ||
            binding.signInConfirmpw.text == binding.signInEditPassword.text ) {
            binding.signInEditPassword.error = "Please cofirm the password"
            binding.signInEditPassword.requestFocus()
            return
        }

        auth!!.createUserWithEmailAndPassword(
            binding.signInEditEmail.text.toString(),
            binding.signInEditPassword.text.toString()
        ).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                checkEmail()
            } else {
                //Toast.makeText(context, binding.signInEditEmail.text.toString(), Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkEmail(){
        val user = auth?.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener{ task->
            if(task.isSuccessful){
                Toast.makeText(requireContext(),"Verification email is send",Toast.LENGTH_SHORT).show()
                auth.signOut()
                var fragment = LogInFragment()
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }
            else{
                Toast.makeText(requireContext(),"Error Occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
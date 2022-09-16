package com.example.noteskeeping

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.noteskeeping.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.combine
import java.util.regex.Pattern

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignInBinding
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        binding.btnBack.setOnClickListener {
            auth.signOut()
            var fragment = LogInFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }
        binding.btnLogin.setOnClickListener {
            signUpUser()
            /*var fragment = LogInFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()*/
        }
    }

    private fun signUpUser() {
        if (binding.editEmailAddressFrg.text.isEmpty()) {
            binding.editEmailAddressFrg.error = "Please enter the Email Address"
            binding.editEmailAddressFrg.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.editEmailAddressFrg.text.toString())
                .matches()
        ) {
            binding.editEmailAddressFrg.error = "Please enter valid Email Address"
            binding.editEmailAddressFrg.requestFocus()
            return
        }
        if(binding.editTextUserName.text.isEmpty()){
            binding.editTextUserName.error = "Please enter User Name"
            binding.editTextUserName.requestFocus()
            return
        }
        if (binding.editNumberPasswordFrg.text.toString().isEmpty()) {
            binding.editNumberPasswordFrg.error = "Please enter the password"
            binding.editNumberPasswordFrg.requestFocus()
            return
        }
        auth!!.createUserWithEmailAndPassword(
            binding.editEmailAddressFrg.text.toString(),
            binding.editNumberPasswordFrg.text.toString()
        ).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                //checkEmail()
            } else {
                Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkEmail(){
        val user = auth?.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener{ task->
            if(task.isSuccessful){
                Toast.makeText(context,"Verification email is send",Toast.LENGTH_SHORT).show()
                auth.signOut()
                var fragment = LogInFragment()
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }
            else{
                Toast.makeText(context,"Error Occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
    }

}
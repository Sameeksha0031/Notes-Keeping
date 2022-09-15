package com.example.noteskeeping

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.noteskeeping.databinding.FragmentLogInBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LogInFragment : Fragment() {
    lateinit var binding: FragmentLogInBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLogInBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        binding.btnsignin.setOnClickListener {
            var fragment = SignInFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer,fragment)?.commit()
        }
        binding.btnloginfrgOne.setOnClickListener {
            doLogin()
            /*var fragment = HomeFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer,fragment)?.commit()*/
        }
    }

    private fun doLogin() {
        if (binding.editemail .text.isEmpty()) {
            binding.editemail.error = "Please enter the Email Address"
            binding.editemail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.editemail.text.toString())
                .matches()
        ) {
            binding.editemail.error = "Please enter valid Email Address"
            binding.editemail.requestFocus()
            return
        }
        if (binding.editpassword.text.toString().isEmpty()) {
            binding.editpassword.error = "Please enter the password"
            binding.editpassword.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(binding.editemail.text.toString(),
            binding.editpassword.text.toString()).addOnCompleteListener(requireActivity()){ task ->
            if(task.isSuccessful){
                Toast.makeText(context,"Login Successful",Toast.LENGTH_SHORT)
                val user = auth.currentUser
                updateUI(user)
            }else{
                Log.w(TAG,"Authentication : Fail",task.exception)
                //Toast.makeText(context,"Login Fail",Toast.LENGTH_SHORT)
                updateUI(null)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            //if(currentUser.isEmailVerified()){
                var fragment = HomeFragment()
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer,fragment)?.commit()
           // }
       // else{
           //     Toast.makeText(context,"Please Verified your Email Address",Toast.LENGTH_SHORT).show()
         //   }
        }else{
            Toast.makeText(context,"Login Fail",Toast.LENGTH_SHORT).show()
        }
    }
}
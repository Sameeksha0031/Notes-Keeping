package com.example.noteskeeping

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteskeeping.databinding.FragmentLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

const val RC_SIGN_IN = 123

class LogInFragment : Fragment() {
    //var checkForLogin : Boolean = false
    lateinit var binding: FragmentLogInBinding
    private lateinit var auth : FirebaseAuth
    //var prg : ProgressDialog ? = null
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
        //prg = ProgressDialog(context)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        binding.googleSignInButton .visibility = View.VISIBLE
        //binding.tvSignIn.visibility = View.GONE
        binding.googleSignInButton.setSize(SignInButton.SIZE_STANDARD)

        binding.googleSignInButton.setOnClickListener{
            var signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            binding.googleSignInButton.visibility = View.GONE
            //binding.tvSignIn.text= acct.displayName
            //binding.tvSignIn.visibility = View.GONE

        }


        binding.loginSignIn.setOnClickListener {
            var fragment = SignUpFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer,fragment)?.commit()
        }
        binding.btnLogin.setOnClickListener {
                doLogin()
        }

        binding.forgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.forgot_password,null)
            builder.setView(view)
            val userEmail : EditText= view.findViewById<EditText>(R.id.reset_password)
            builder.setPositiveButton("Reset",DialogInterface.OnClickListener{ _, _ ->
                forgotPassword(userEmail)
            })
            builder.setNegativeButton("Close",DialogInterface.OnClickListener{ _, _ ->})
            builder.show()
        }
    }

    private fun forgotPassword(userEmail: EditText) {
        if (userEmail.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.text.toString())
                .matches()
        ) {
            return
        }
        //Toast.makeText(context,"Email is sent",Toast.LENGTH_SHORT).show()
        auth.sendPasswordResetEmail(userEmail.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context,"Email is sent",Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun doLogin() {
        //prg?.setMessage("Login")
        //prg?.show()
        if (binding.loginEditEmail.text.toString().isEmpty()) {
            binding.loginEditEmail.error = "Please enter the Email Address"
            binding.loginEditEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.loginEditEmail.text.toString())
                .matches()
        ) {
            binding.loginEditEmail.error = "Please enter valid Email Address"
            binding.loginEditEmail.requestFocus()
            return
        }
        if (binding.loginEditPassword.text.toString().isEmpty()) {
            binding.loginEditPassword.error = "Please enter the password"
            binding.loginEditPassword.requestFocus()
            return
        }
        auth!!.signInWithEmailAndPassword(binding.loginEditEmail.text.toString(),
            binding.loginEditPassword.text.toString()).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                //prg?.dismiss()
                Toast.makeText(context,"Login Successful",Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                updateUI(user)
            }else{
                //prg?.dismiss()
                //Log.w(TAG,"Authentication : Fail",task.exception)
                updateUI(null)
                Toast.makeText(context,"Login Fail",Toast.LENGTH_SHORT).show()
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
            verifyEmail()
        }
    }
    fun verifyEmail(){
        val user = auth.currentUser
        val vemail : Boolean? = user?.isEmailVerified
        val intent = Intent(context,HomeActivity::class.java)
        startActivity(intent)
        if(vemail!!){
            //fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer,fragment)?.commit()
            startActivity(intent)
        }else{
            Toast.makeText(context,"Please Verified your Email Address",Toast.LENGTH_SHORT).show()
            auth.signOut()
        }
    }
    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        val intent = Intent(context,HomeActivity::class.java)
        try {
            val account = completedTask.getResult(ApiException::class.java)
            binding.googleSignInButton.visibility  = View.GONE
            startActivity(intent)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            binding.googleSignInButton.visibility  = View.GONE
            startActivity(intent)
        }
    }
}
package com.example.noteskeeping.view

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentLogInBinding
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices
import com.example.noteskeeping.view.HomeActivity
import com.example.noteskeeping.viewModel.LogInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

const val RC_SIGN_IN = 123

class LogInFragment : Fragment() {
    //var checkForLogin : Boolean = false
    lateinit var binding: FragmentLogInBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var logInViewModel: LogInViewModel
    private lateinit var userAuthServices: UserAuthServices // doubt
    private lateinit var googleSignInClient: GoogleSignInClient
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
        logInViewModel = LogInViewModel(UserAuthServices())
        userAuthServices = UserAuthServices() //doubt
        //prg = ProgressDialog(context)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        binding.googleSignInButton.setOnClickListener{
            signInGoogle()
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
        var userName = ""
        var userEmail = ""
        var userPassword = ""
        //prg?.setMessage("Login")
        //prg?.show()
        userEmail = binding.loginEditEmail.text.toString().trim()
        userPassword = binding.loginEditPassword.text.toString().trim()

        val user = User(userName = userName, email = userEmail, password = userPassword)

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

        logInViewModel.loginUser(user)
        logInViewModel.userLogin.observe(viewLifecycleOwner, Observer {
            if(it.status){
                Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        userAuthServices.checkingForUser {
            if(it.status){
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
               if(result.resultCode == Activity.RESULT_OK){
                   val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                   handleResults(task)
               }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if(account != null){
                updateUIForGoogle(account)
            }
        }else{
            Toast.makeText(requireContext(),task.exception.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUIForGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val intent : Intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(requireContext(),it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}
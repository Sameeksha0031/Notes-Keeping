package com.example.noteskeeping.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteskeeping.model.AuthListener
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices

class LogInViewModel(private var userAuthServices: UserAuthServices) : ViewModel(){
    var _UserLogin = MutableLiveData<AuthListener>()
    val userLogin = _UserLogin as LiveData<AuthListener>

    fun loginUser(user: User){
        userAuthServices.userLogin(user){
            if(it.status){
                _UserLogin.value = it
            }
        }

    }
}
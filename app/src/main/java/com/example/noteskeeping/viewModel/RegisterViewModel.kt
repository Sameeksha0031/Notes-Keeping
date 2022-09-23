package com.example.noteskeeping.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteskeeping.model.AuthListener
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices

class RegisterViewModel(private var userAuthServices: UserAuthServices) : ViewModel() {
    private var _UserRegisterStatus = MutableLiveData<AuthListener>()
    val userRegisterStatus = _UserRegisterStatus as LiveData<AuthListener>

    fun registerUser(user: User){
        userAuthServices.userRegistration(user){
            if(it.status){
                _UserRegisterStatus.value = it
            }
        }

    }
}
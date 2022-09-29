package com.example.noteskeeping.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteskeeping.model.AuthListener
import com.example.noteskeeping.model.UserAuthServices

class CustomDialogModelFactory(private  val userAuthServices: UserAuthServices) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CustomDialogModel(userAuthServices) as T
    }
}
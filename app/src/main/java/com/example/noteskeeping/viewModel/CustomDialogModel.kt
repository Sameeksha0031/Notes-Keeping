package com.example.noteskeeping.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteskeeping.model.AuthListener
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices

class CustomDialogModel(private var userAuthServices: UserAuthServices) : ViewModel(){
    var _ProfileView = MutableLiveData<AuthListener>()
    var profileView = _ProfileView as LiveData<AuthListener>
    //fun profileView(){
      // userAuthServices.readFireStore()
    //}
}
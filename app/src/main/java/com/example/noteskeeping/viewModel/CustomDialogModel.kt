package com.example.noteskeeping.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteskeeping.model.DialogAuthListener
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices

class CustomDialogModel(private var userAuthServices: UserAuthServices) : ViewModel(){
    var _ProfileView = MutableLiveData<DialogAuthListener>()
    var profileView = _ProfileView as LiveData<DialogAuthListener>

    fun viewProfile(user : User){
        userAuthServices.readFireStore(user = user){
            if(it.status){
                _ProfileView.value = it
            }
        }
    }
}
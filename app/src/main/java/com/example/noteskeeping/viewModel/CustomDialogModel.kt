package com.example.noteskeeping.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteskeeping.model.AuthListener
import com.example.noteskeeping.model.DialogAuthListener
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices

class CustomDialogModel(private var userAuthServices: UserAuthServices) : ViewModel(){
    var _ProfileView = MutableLiveData<DialogAuthListener>()
    var _AddProfile =  MutableLiveData<AuthListener>()
    var profileView = _ProfileView as LiveData<DialogAuthListener>
    var profileImage = _AddProfile as LiveData<AuthListener>

    fun viewProfile(user : User){
        userAuthServices.readFireStore(user = user){
            if(it.status){
                _ProfileView.value = it
            }
        }
    }

    fun changeProfileImage(user: User, filePath : Uri){
            userAuthServices.uploadImage(user, filePath){
                if(it.status){
                    _AddProfile.value = it
                }
            }
    }
}

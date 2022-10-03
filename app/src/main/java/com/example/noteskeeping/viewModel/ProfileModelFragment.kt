package com.example.noteskeeping.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteskeeping.model.AuthListener
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices

class ProfileModelFragment(private var userAuthServices: UserAuthServices) : ViewModel(){
    var _UserProfile = MutableLiveData<AuthListener>()
    var profileUser = _UserProfile as LiveData<AuthListener>

    fun saveImage(user: User,uri : Uri){
        userAuthServices.uploadImage(user,uri){
            if(it.status){
                _UserProfile.value = it
            }
        }
    }

}
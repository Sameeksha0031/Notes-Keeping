package com.example.noteskeeping.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteskeeping.model.UserAuthServices

class ProfileModelViewFactory(private  val userAuthServices: UserAuthServices) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileModelFragment(userAuthServices) as T
    }
}

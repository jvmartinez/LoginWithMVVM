package com.jvmartinez.loginwithmvvm.ui.home

import androidx.lifecycle.ViewModel
import com.jvmartinez.loginwithmvvm.core.data.repository.UserRepository

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {
    val user by lazy {
        repository.currentUser()
    }

    fun onAttach() {

    }

    fun logout() {
        repository.logout()
    }

    fun getEmailUser(): String? {
        return  user?.email
    }


}
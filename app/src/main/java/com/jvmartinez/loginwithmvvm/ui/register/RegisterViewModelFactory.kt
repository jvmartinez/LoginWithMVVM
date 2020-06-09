package com.jvmartinez.loginwithmvvm.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jvmartinez.loginwithmvvm.core.data.repository.UserRepository

class RegisterViewModelFactory(private val repository: UserRepository, private val registerInteractor: RegisterInteractor): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterViewModel(repository, registerInteractor) as T
    }
}
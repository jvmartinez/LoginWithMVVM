package com.jvmartinez.loginwithmvvm.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jvmartinez.loginwithmvvm.core.data.repository.UserRepository

class HomeViewModelFactory(private val repository: UserRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
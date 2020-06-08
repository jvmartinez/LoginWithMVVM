package com.jvmartinez.loginwithmvvm.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jvmartinez.loginwithmvvm.util.ScreenState

class LoginViewModel(private val loginInteractor: LoginInteractor) : ViewModel(),
    LoginInteractor.OnLoginFinishedListener {

    private val mutableLoginState: MutableLiveData<ScreenState<LoginState>> = MutableLiveData()

    val loginState: LiveData<ScreenState<LoginState>>
        get() = mutableLoginState

    fun onLoginClicked(username: String, password: String) {
        mutableLoginState.value = ScreenState.Loading
        loginInteractor.login(username, password, this)
    }

    override fun onUsernameError() {
        mutableLoginState.value = ScreenState.Render(LoginState.WrongUserName)
    }

    override fun onPasswordError() {
        mutableLoginState.value = ScreenState.Render(LoginState.WrongPassword)
    }

    override fun onSuccess() {
        mutableLoginState.value = ScreenState.Render(LoginState.Success)
    }
}

class LoginViewModelFactory(private val loginInteractor: LoginInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginInteractor) as T
    }
}
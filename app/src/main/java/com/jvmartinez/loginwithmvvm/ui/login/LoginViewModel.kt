package com.jvmartinez.loginwithmvvm.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jvmartinez.loginwithmvvm.core.data.repository.UserRepository
import com.jvmartinez.loginwithmvvm.util.ScreenState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val loginInteractor: LoginInteractor, private val repository: UserRepository) : ViewModel(),
    LoginInteractor.OnLoginFinishedListener {

    private val loginLiveData: MutableLiveData<ScreenState<LoginState>> = MutableLiveData()
    private var email: String = ""
    private var password: String = ""
    private val loginStateButton: MutableLiveData<Boolean> = MutableLiveData()
    private val disposables = CompositeDisposable()

    val loginState: LiveData<ScreenState<LoginState>>
        get() = loginLiveData

    fun onLoginClicked() {
        loginLiveData.value = ScreenState.Loading
        loginInteractor.login(email, password, this)
    }

    override fun onUserError() {
        loginLiveData.value = ScreenState.Render(LoginState.WrongEmail)
    }

    override fun onPasswordLengthError() {
        loginLiveData.value = ScreenState.Render(LoginState.WrongPasswordLength)
    }

    override fun onSuccess() {
        val disposable = repository.login(email, password)
            .subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loginLiveData.value = ScreenState.Render(LoginState.Success)
            }, {
                loginLiveData.value = ScreenState.Render(LoginState.WrongEmail)
            })
        disposables.add(disposable)
    }

    fun userTextChange(toString: String) {
        email = toString
        setLoginStateButton(email.isNotEmpty() && password.isNotEmpty())
    }

    fun passwordTextChange(toString: String) {
        password = toString
        setLoginStateButton(email.isNotEmpty() && password.isNotEmpty())
    }

    fun getLoginStateButton(): LiveData<Boolean> {
        return loginStateButton
    }

    fun setLoginStateButton(state: Boolean?){
        this.loginStateButton.value = state
    }
}

class LoginViewModelFactory(
    private val loginInteractor: LoginInteractor,
    private val repository: UserRepository
):
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginInteractor, repository) as T
    }
}
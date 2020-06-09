package com.jvmartinez.loginwithmvvm.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jvmartinez.loginwithmvvm.core.data.repository.UserRepository
import com.jvmartinez.loginwithmvvm.util.ScreenState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegisterViewModel(private val repository: UserRepository, private val registerInteractor: RegisterInteractor): ViewModel(), RegisterInteractor.OnLoginFinishedListener {
    private var email: String = ""
    private var password: String = ""
    private var registerStateButton: MutableLiveData<Boolean> = MutableLiveData()
    private val registerLiveData: MutableLiveData<ScreenState<RegisterState>> = MutableLiveData()
    private val disposables = CompositeDisposable()

    val registerState: LiveData<ScreenState<RegisterState>>
        get() = registerLiveData

    fun emailTextChange(toString: String) {
        email = toString
        setRegisterStateButton(email.isNotEmpty() && password.isNotEmpty())
    }

    fun passwordTextChange(toString: String) {
        password = toString
        setRegisterStateButton(email.isNotEmpty() && password.isNotEmpty())
    }

    fun getRegisterStateButton(): LiveData<Boolean>{
        return registerStateButton
    }

    fun setRegisterStateButton(enable: Boolean?) {
        registerStateButton.value = enable
    }

    override fun onEmailInvalid() {
        registerLiveData.value = ScreenState.Render(RegisterState.EmailInvalid)
    }

    override fun onPasswordLengthError() {
        registerLiveData.value = ScreenState.Render(RegisterState.PasswordLength)
    }

    override fun onSuccess() {
        val disposable = repository.register(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                registerLiveData.value = ScreenState.Render(RegisterState.Success)
            }, {
                registerLiveData.value = ScreenState.Render(RegisterState.ErrorRegister)
            })
        disposables.add(disposable)
    }

    fun onRegisterClicked() {
        registerLiveData.value = ScreenState.Loading
        registerInteractor.register(email, password, this)
    }
}
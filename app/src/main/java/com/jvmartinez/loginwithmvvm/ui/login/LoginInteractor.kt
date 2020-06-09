package com.jvmartinez.loginwithmvvm.ui.login

class LoginInteractor {
    interface OnLoginFinishedListener {
        fun onUserError()
        fun onPasswordLengthError()
        fun onSuccess()
    }

    fun login(username: String, password: String, listener: OnLoginFinishedListener) {
        when {
            password.length <= 5 -> listener.onPasswordLengthError()
            else -> listener.onSuccess()
        }
    }
}
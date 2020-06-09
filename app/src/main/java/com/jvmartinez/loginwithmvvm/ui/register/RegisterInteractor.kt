package com.jvmartinez.loginwithmvvm.ui.register

class RegisterInteractor {
    interface OnLoginFinishedListener {
        fun onEmailInvalid()
        fun onPasswordLengthError()
        fun onSuccess()
    }

    fun register(email: String, password: String, listener: OnLoginFinishedListener) {
        when {
            !isEmail(email) -> listener.onEmailInvalid()
            password.length < 6 -> listener.onPasswordLengthError()
            else -> listener.onSuccess()
        }
    }
    private fun isEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
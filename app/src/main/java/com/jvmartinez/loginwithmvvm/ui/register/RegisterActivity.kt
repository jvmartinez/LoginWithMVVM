package com.jvmartinez.loginwithmvvm.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jvmartinez.loginwithmvvm.R
import com.jvmartinez.loginwithmvvm.core.data.firebase.FirebaseSource
import com.jvmartinez.loginwithmvvm.core.data.repository.UserRepository
import com.jvmartinez.loginwithmvvm.ui.home.HomeActivity
import com.jvmartinez.loginwithmvvm.ui.login.LoginActivity
import com.jvmartinez.loginwithmvvm.util.ScreenState
import com.jvmartinez.loginwithmvvm.util.Utilities
import kotlinx.android.synthetic.main.content_login.loading
import kotlinx.android.synthetic.main.content_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProvider(this,
            RegisterViewModelFactory(UserRepository(FirebaseSource()), RegisterInteractor()))
            .get(RegisterViewModel::class.java)
        onObserve()
        onClickListener()

    }

    private fun textChangedEmail(): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.emailTextChange(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    private fun textChangedPassword(): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.passwordTextChange(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    private fun onObserve() {
        viewModel.registerState.observe(::getLifecycle, ::updateUI)
        viewModel.getRegisterStateButton().observe(this, Observer { enabled ->
            btn_register_user.isEnabled = enabled
        })
    }
    private fun onClickListener() {
        btn_register_user.setOnClickListener { onRegisterClicked() }
        txt_user_register.addTextChangedListener(textChangedEmail())
        txt_password_register.addTextChangedListener(textChangedPassword())
    }

    private fun updateUI(screenState: ScreenState<RegisterState>?) {
        when (screenState) {
            ScreenState.Loading -> { loading.visibility = View.VISIBLE }
            is ScreenState.Render -> processRegisterState(screenState.renderState)
        }
    }

    private fun processRegisterState(renderState: RegisterState) {
        loading.visibility = View.GONE
        when (renderState) {
            RegisterState.Success -> startActivity(Intent(this, LoginActivity::class.java))
            RegisterState.EmailInvalid -> Utilities().alert(getString(R.string.message_error_email_register), this)
            RegisterState.PasswordLength -> Utilities().alert(getString(R.string.message_error_password_length), this)
            RegisterState.ErrorRegister -> Utilities().alert(getString(R.string.lbl_message), this)

        }
    }

    private fun onRegisterClicked() {
        hideKeyboard()
        viewModel.onRegisterClicked()
    }

    fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
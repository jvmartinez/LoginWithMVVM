package com.jvmartinez.loginwithmvvm.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jvmartinez.loginwithmvvm.R
import com.jvmartinez.loginwithmvvm.core.data.firebase.FirebaseSource
import com.jvmartinez.loginwithmvvm.core.data.repository.UserRepository
import com.jvmartinez.loginwithmvvm.ui.home.HomeActivity
import com.jvmartinez.loginwithmvvm.util.ScreenState
import kotlinx.android.synthetic.main.content_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(
            this,
            LoginViewModelFactory(LoginInteractor(), UserRepository(FirebaseSource()))
        )[LoginViewModel::class.java]
        onObserve()
        onClickListener()
    }

    private fun updateUI(screenState: ScreenState<LoginState>?) {
        when (screenState) {
            ScreenState.Loading -> { loading.visibility = View.VISIBLE }
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: LoginState) {
        loading.visibility = View.GONE
        when (renderState) {
            LoginState.Success -> startActivity(Intent(this, HomeActivity::class.java))
            LoginState.WrongUser -> alert(getString(R.string.message_error_user_and_password))
            LoginState.WrongPasswordLength -> alert(getString(R.string.message_error_password_length))
        }
    }

    private fun alert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage(message)
            .setPositiveButton(R.string.btn_ok) { dialog, _ -> dialog.dismiss() }
        builder.context.setTheme(R.style.AppTheme_Dialog)
        builder.create().show()
    }

    private fun onLoginClicked() {
        hideKeyboard()
        viewModel.onLoginClicked()
    }


    private fun textChangedUser(): TextWatcher? {
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
                viewModel.userTextChange(s.toString())
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
        viewModel.loginState.observe(::getLifecycle, ::updateUI)
        viewModel.getLoginStateButton().observe(this, Observer { enabled ->
            btn_login.isEnabled = enabled
        })
    }
    private fun onClickListener() {
        btn_login.setOnClickListener { onLoginClicked() }
        txt_user.addTextChangedListener(textChangedUser())
        txt_password.addTextChangedListener(textChangedPassword())
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
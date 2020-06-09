package com.jvmartinez.loginwithmvvm.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.jvmartinez.loginwithmvvm.R
import com.jvmartinez.loginwithmvvm.core.data.firebase.FirebaseSource
import com.jvmartinez.loginwithmvvm.core.data.repository.UserRepository
import com.jvmartinez.loginwithmvvm.ui.login.LoginActivity
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(UserRepository(FirebaseSource()))
        ).get(HomeViewModel::class.java)
        showInfoUser()
    }

    private fun showInfoUser() {
        lbl_name_user.text = viewModel.getEmailUser()
    }

    private fun showLoginActivity() {
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> showLoginActivity()
        }
        return super.onOptionsItemSelected(item)

    }
}
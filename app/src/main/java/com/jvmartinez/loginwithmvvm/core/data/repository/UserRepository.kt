package com.jvmartinez.loginwithmvvm.core.data.repository

import com.jvmartinez.loginwithmvvm.core.data.firebase.FirebaseSource

class UserRepository(private val firebase: FirebaseSource) {
    fun login(email: String, password: String) = firebase.login(email, password)
    fun currentUser() = firebase.currentUser()
    fun logout() = firebase.logout()
    fun register(email: String, password: String) = firebase.register(email, password)
}
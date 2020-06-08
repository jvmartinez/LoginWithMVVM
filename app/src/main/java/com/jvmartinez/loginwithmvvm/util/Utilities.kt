package com.jvmartinez.loginwithmvvm.util

import android.os.Handler

class Utilities {

    fun postDelayed(delayMillis: Long, task: () -> Unit) {
        Handler().postDelayed(task, delayMillis)
    }
}
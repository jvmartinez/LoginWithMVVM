package com.jvmartinez.loginwithmvvm.util

import android.content.Context
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import com.jvmartinez.loginwithmvvm.R

class Utilities {

    fun postDelayed(delayMillis: Long, task: () -> Unit) {
        Handler().postDelayed(task, delayMillis)
    }

    fun alert(message: String, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setMessage(message)
            .setPositiveButton(R.string.btn_ok) { dialog, _ -> dialog.dismiss() }
        builder.context.setTheme(R.style.AppTheme_Dialog)
        builder.create().show()
    }
}
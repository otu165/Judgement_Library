package com.example.judgement.extension

import android.app.Activity
import android.util.Log
import android.widget.Adapter
import androidx.fragment.app.Fragment
import com.example.judgement.BuildConfig

/* extension for debug log message*/
fun Any.logd(message: String) {
    if (BuildConfig.DEBUG) {
        val simpleName = this::class.java.simpleName
        val tag = if (simpleName.length <= 23) simpleName else simpleName.substring(0, 23)

        Log.d(tag, message)
    }
}



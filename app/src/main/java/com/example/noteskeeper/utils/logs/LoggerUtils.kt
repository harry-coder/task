package com.example.noteskeeper.utils.logs

import android.text.TextUtils
import android.util.Log

object LoggerUtils {
    // Verbose Logs
    @JvmStatic
    fun logVerbose(key: String, value: String?) {
        Log.v(key, value ?: "")
    }
}
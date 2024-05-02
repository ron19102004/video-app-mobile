package com.video.app.states.objects

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.video.app.services.NotificationService

@SuppressLint("StaticFieldLeak")
object UiState {
    object SharedPreferencesKey {
        const val ROOT = "ui"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var _context: Context

    fun init(context: Context) {
        _context = context
        sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKey.ROOT, Context.MODE_PRIVATE)
    }
}
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
        const val IS_DARK_MODE = "dark-mode"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var _context: Context
    var darkMode by mutableStateOf(false)
    fun init(context: Context) {
        _context = context
        sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKey.ROOT, Context.MODE_PRIVATE)
        darkMode = sharedPreferences.getBoolean(SharedPreferencesKey.IS_DARK_MODE, false)
    }

    fun changeDarkMode(value: Boolean) {
        darkMode = value
        sharedPreferences.edit()
            .putBoolean(SharedPreferencesKey.IS_DARK_MODE, value)
            .apply()
        NotificationService.ShowMessage.show(
            _context,
            "UI notification",
            "Dark mode is turning ${if (darkMode) "on" else "off"}"
        )
    }
}
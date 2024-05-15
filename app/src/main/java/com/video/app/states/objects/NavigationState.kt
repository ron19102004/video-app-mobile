package com.video.app.states.objects

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

object NavigationState {
    var navSelected by mutableIntStateOf(1)
}
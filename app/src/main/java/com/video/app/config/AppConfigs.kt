package com.video.app.config

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class CONSTANT {
    object UI {
        val ROUNDED_INPUT_BUTTON: Dp = 10.dp

        val HEIGHT_INPUT: Dp = 70.dp
        val HEIGHT_BUTTON: Dp = 60.dp
        object TEXT_SIZE {
            val SM:TextUnit = 15.sp
            val MD:TextUnit = 20.sp
            val LG:TextUnit = 25.sp
            val XL:TextUnit = 35.sp
        }
    }
}
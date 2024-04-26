package com.video.app.config

object ValidRegex {
    private val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private val phonePattern = Regex("^(\\+[0-9]{1,3})?[0-9]{10,}$")
    fun isEmail(value: String): Boolean {
        return emailPattern.matches(value)
    }

    fun isPhone(value: String): Boolean {
        return phonePattern.matches(value)
    }
}
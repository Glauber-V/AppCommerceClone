package com.example.appcommerceclone.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeExt {

    fun getCurrentTime(): String {
        val currentTime = LocalDateTime.now()
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return formatter.format(currentTime)
    }
}
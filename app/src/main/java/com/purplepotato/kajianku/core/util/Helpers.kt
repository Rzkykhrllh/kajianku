package com.purplepotato.kajianku.core.util

import android.annotation.SuppressLint
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

object Helpers {
    @SuppressLint("SimpleDateFormat")
    fun changeDateFormat(text: String): String? {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("MMMM dd, yyyy")
            val date = parser.parse(text)
            formatter.format(date as Date)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
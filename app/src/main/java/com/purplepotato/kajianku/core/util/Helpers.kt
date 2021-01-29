package com.purplepotato.kajianku.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object Helpers {
    @SuppressLint("SimpleDateFormat")
    fun convertTimeStampToDateFormat(timeInMillis: Long): String {
        return try {
            val parser = SimpleDateFormat("EEEE, dd MMMM YYYY", Locale.getDefault())
            parser.format(timeInMillis)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun convertTimeStampToDateTimeFormat(timeInMillis: Long): String {
        return try {
            val parser = SimpleDateFormat("dd MMMM YYYY | HH.mm", Locale.getDefault())
            parser.format(timeInMillis)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertTimeStampToTimeFormat(timeInMillis: Long): String {
        return try {
            val sdf = SimpleDateFormat("HH.mm")
            val dateTime = Date(timeInMillis)
            sdf.format(dateTime)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Context.showLongToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showShortToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
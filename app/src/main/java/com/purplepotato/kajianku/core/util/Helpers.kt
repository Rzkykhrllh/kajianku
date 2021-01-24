package com.purplepotato.kajianku.core.util

import android.util.Patterns

object Helpers {

}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
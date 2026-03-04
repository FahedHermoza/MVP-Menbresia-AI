package com.fahed.perupass.data.repository

import android.annotation.SuppressLint
import android.content.Context

// Set in Activity.onResume(), cleared in Activity.onPause() to avoid memory leaks.
@SuppressLint("StaticFieldLeak")
object AuthContextHolder {
    var activityContext: Context? = null
}

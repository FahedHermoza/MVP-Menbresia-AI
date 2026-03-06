package com.fahed.perupass.feature.screen.detail.model

import android.annotation.SuppressLint

object DistanceHelper {
    @SuppressLint("DefaultLocale")
    fun format(meters: Float): String = when {
        meters < 1000 -> "${meters.toInt()}m away"
        else -> String.format("%.1fkm away", meters / 1000)
    }
}

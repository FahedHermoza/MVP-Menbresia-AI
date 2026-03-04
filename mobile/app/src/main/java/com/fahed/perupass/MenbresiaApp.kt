package com.fahed.perupass

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Menbresia AI.
 *
 * @HiltAndroidApp triggers Hilt's code generation to create the application-level
 * DI component which is the root of the dependency injection hierarchy.
 */
@HiltAndroidApp
class MenbresiaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase init — required for Auth, Firestore, Storage
        FirebaseApp.initializeApp(this)
    }
}

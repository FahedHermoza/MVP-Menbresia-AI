package com.fahed.perupass

import android.app.Application
import com.google.firebase.FirebaseApp

class MenbresiaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicialización de Firebase al arrancar la aplicación
        FirebaseApp.initializeApp(this)
    }
}

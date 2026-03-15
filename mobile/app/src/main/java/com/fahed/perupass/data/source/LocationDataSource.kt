package com.fahed.perupass.data.source

import android.annotation.SuppressLint
import android.os.Looper
import com.fahed.perupass.domain.model.UserLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataSource @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    fun locationFlow(): Flow<UserLocation> = callbackFlow {
        val fiveMinutes = 300_000L
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, fiveMinutes)
            .setMinUpdateIntervalMillis(fiveMinutes)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(UserLocation(location.latitude, location.longitude))
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )

        awaitClose { fusedLocationClient.removeLocationUpdates(callback) }
    }

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): UserLocation? {
        val location = fusedLocationClient.lastLocation.await() ?: return null
        return UserLocation(location.latitude, location.longitude)
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocationOnce(): UserLocation {
        val cts = CancellationTokenSource()
        val location = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cts.token
        ).await() ?: error("No se pudo obtener la ubicación")
        return UserLocation(location.latitude, location.longitude)
    }
}

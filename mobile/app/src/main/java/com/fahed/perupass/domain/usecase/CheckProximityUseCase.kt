package com.fahed.perupass.domain.usecase

import com.fahed.perupass.domain.model.LatLng
import javax.inject.Inject
import kotlin.math.*

data class ProximityResult(
    val distanceMeters: Float,
    val isInRange: Boolean
)

class CheckProximityUseCase @Inject constructor() {
    operator fun invoke(userLocation: LatLng, venueLocation: LatLng): ProximityResult {
        val earthRadius = 6371e3 // in meters
        
        val lat1 = Math.toRadians(userLocation.latitude)
        val lat2 = Math.toRadians(venueLocation.latitude)
        val dLat = Math.toRadians(venueLocation.latitude - userLocation.latitude)
        val dLon = Math.toRadians(venueLocation.longitude - userLocation.longitude)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = (earthRadius * c).toFloat()
        
        return ProximityResult(
            distanceMeters = distance,
            isInRange = distance < 50f
        )
    }
}

package com.fahed.perupass.domain.usecase

import com.fahed.perupass.core.location.HaversineHelper
import com.fahed.perupass.domain.model.ProximityResult
import javax.inject.Inject

private const val GEOFENCE_RADIUS_METERS = 50f

class CheckProximityUseCase @Inject constructor() {

    operator fun invoke(
        userLat: Double,
        userLng: Double,
        venueLat: Double,
        venueLng: Double
    ): ProximityResult {
        val distanceMeters = HaversineHelper.distanceMeters(userLat, userLng, venueLat, venueLng).toFloat()
        return ProximityResult(
            distanceMeters = distanceMeters,
            isInRange = distanceMeters < GEOFENCE_RADIUS_METERS
        )
    }
}

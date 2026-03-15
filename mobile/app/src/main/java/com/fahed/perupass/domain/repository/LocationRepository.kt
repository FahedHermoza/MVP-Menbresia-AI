package com.fahed.perupass.domain.repository

import com.fahed.perupass.domain.model.UserLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentLocation(): Flow<UserLocation>
    suspend fun getLastKnownLocation(): UserLocation?
    suspend fun getLocationOnce(): UserLocation
}

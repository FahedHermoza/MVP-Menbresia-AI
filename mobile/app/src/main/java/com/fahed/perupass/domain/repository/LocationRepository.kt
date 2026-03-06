package com.fahed.perupass.domain.repository

import com.fahed.perupass.domain.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentLocation(): Flow<LatLng>
}

package com.fahed.perupass.data.repository

import com.fahed.perupass.data.source.LocationDataSource
import com.fahed.perupass.domain.model.LatLng
import com.fahed.perupass.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    override fun getCurrentLocation(): Flow<LatLng> {
        return locationDataSource.getLocationFlow()
    }
}

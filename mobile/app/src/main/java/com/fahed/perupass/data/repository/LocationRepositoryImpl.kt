package com.fahed.perupass.data.repository

import com.fahed.perupass.data.source.LocationDataSource
import com.fahed.perupass.domain.model.UserLocation
import com.fahed.perupass.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
) : LocationRepository {

    override fun getCurrentLocation(): Flow<UserLocation> =
        locationDataSource.locationFlow()
}

package com.fahed.perupass.domain.usecase

import com.fahed.perupass.domain.model.UserLocation
import com.fahed.perupass.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): UserLocation = locationRepository.getLocationOnce()
}

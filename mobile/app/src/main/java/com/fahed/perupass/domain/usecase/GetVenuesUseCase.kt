package com.fahed.perupass.domain.usecase

import com.fahed.perupass.domain.model.Venue
import com.fahed.perupass.domain.repository.VenueRepository
import javax.inject.Inject

class GetVenuesUseCase @Inject constructor(
    private val venueRepository: VenueRepository
) {
    suspend operator fun invoke(): Result<List<Venue>> = venueRepository.getVenues()
}

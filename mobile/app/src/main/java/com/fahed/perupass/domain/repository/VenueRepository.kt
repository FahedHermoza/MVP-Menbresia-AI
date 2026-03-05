package com.fahed.perupass.domain.repository

import com.fahed.perupass.domain.model.Venue

interface VenueRepository {
    suspend fun getVenues(): Result<List<Venue>>
    suspend fun getVenueById(id: String): Result<Venue>
}

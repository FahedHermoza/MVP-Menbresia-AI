package com.fahed.perupass.data.repository

import com.fahed.perupass.data.model.mapper.toDomain
import com.fahed.perupass.data.source.remote.VenueRemoteDataSource
import com.fahed.perupass.domain.model.Venue
import com.fahed.perupass.domain.repository.VenueRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VenueRepositoryImpl @Inject constructor(
    private val remoteDataSource: VenueRemoteDataSource
) : VenueRepository {

    override suspend fun getVenues(): Result<List<Venue>> = runCatching {
        remoteDataSource.getVenues().map { it.toDomain() }
    }

    override suspend fun getVenueById(id: String): Result<Venue> = runCatching {
        remoteDataSource.getVenueById(id)?.toDomain()
            ?: error("Venue not found: $id")
    }
}

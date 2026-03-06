package com.fahed.perupass.data.repository

import com.fahed.perupass.data.source.remote.ValidationRemoteDataSource
import com.fahed.perupass.domain.repository.ValidationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidationRepositoryImpl @Inject constructor(
    private val remoteDataSource: ValidationRemoteDataSource
) : ValidationRepository {

    override suspend fun validatePin(venueId: String, enteredPin: String): Result<Boolean> =
        runCatching {
            val venuePin = remoteDataSource.getVenuePin(venueId)
                ?: return Result.failure(NoSuchElementException("PIN not found for venue $venueId"))
            venuePin == enteredPin
        }

    override suspend fun logValidation(venueId: String, venueName: String, benefit: String): Result<Unit> =
        runCatching {
            remoteDataSource.logValidation(venueId, venueName, benefit)
        }
}

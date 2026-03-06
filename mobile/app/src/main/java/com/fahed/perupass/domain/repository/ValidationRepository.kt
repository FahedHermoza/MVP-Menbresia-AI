package com.fahed.perupass.domain.repository

interface ValidationRepository {
    suspend fun validatePin(venueId: String, enteredPin: String): Result<Boolean>
    suspend fun logValidation(venueId: String, venueName: String, benefit: String): Result<Unit>
}

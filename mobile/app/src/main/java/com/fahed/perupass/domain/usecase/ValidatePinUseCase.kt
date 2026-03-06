package com.fahed.perupass.domain.usecase

import com.fahed.perupass.domain.model.ValidationResult
import com.fahed.perupass.domain.repository.ValidationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidatePinUseCase @Inject constructor(
    private val validationRepository: ValidationRepository
) {
    companion object {
        private const val MAX_ATTEMPTS = 3
        private const val BLOCK_DURATION_SECONDS = 30
        private const val SUCCESS_DURATION_SECONDS = 300
    }

    private var attemptCount = 0
    private var blockedUntilMillis = 0L

    suspend operator fun invoke(
        venueId: String,
        enteredPin: String,
        venueName: String,
        benefit: String
    ): ValidationResult {
        val nowMillis = System.currentTimeMillis()
        if (nowMillis < blockedUntilMillis) {
            val remainingSeconds = ((blockedUntilMillis - nowMillis) / 1000).toInt()
            return ValidationResult.Blocked(remainingSeconds)
        }

        return validationRepository.validatePin(venueId, enteredPin).fold(
            onSuccess = { isValid ->
                if (isValid) {
                    attemptCount = 0
                    blockedUntilMillis = 0L
                    validationRepository.logValidation(venueId, venueName, benefit)
                    ValidationResult.Success(benefit, SUCCESS_DURATION_SECONDS)
                } else {
                    handleWrongPin()
                }
            },
            onFailure = { handleWrongPin() }
        )
    }

    private fun handleWrongPin(): ValidationResult {
        attemptCount++
        return if (attemptCount >= MAX_ATTEMPTS) {
            blockedUntilMillis = System.currentTimeMillis() + BLOCK_DURATION_SECONDS * 1000L
            attemptCount = 0
            ValidationResult.Blocked(BLOCK_DURATION_SECONDS)
        } else {
            ValidationResult.WrongPin(MAX_ATTEMPTS - attemptCount)
        }
    }
}

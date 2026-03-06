package com.fahed.perupass

import com.fahed.perupass.domain.model.ValidationResult
import com.fahed.perupass.domain.repository.ValidationRepository
import com.fahed.perupass.domain.usecase.ValidatePinUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidatePinUseCaseTest {

    private val validationRepository: ValidationRepository = mockk()
    private lateinit var useCase: ValidatePinUseCase

    @Before
    fun setUp() {
        useCase = ValidatePinUseCase(validationRepository)
    }

    @Test
    fun `given correct pin returns Success with benefit`() = runTest {
        coEvery { validationRepository.validatePin("v1", "1234") } returns Result.success(true)
        coEvery { validationRepository.logValidation(any(), any(), any()) } returns Result.success(Unit)

        val result = useCase("v1", "1234", "CHANGU CLUB", "2x1 Gin Tonic")

        assertTrue(result is ValidationResult.Success)
        assertEquals("2x1 Gin Tonic", (result as ValidationResult.Success).benefit)
        assertEquals(300, result.remainingSeconds)
    }

    @Test
    fun `given wrong pin returns WrongPin with 2 remaining`() = runTest {
        coEvery { validationRepository.validatePin("v1", "9999") } returns Result.success(false)

        val result = useCase("v1", "9999", "CHANGU CLUB", "2x1 Gin Tonic")

        assertTrue(result is ValidationResult.WrongPin)
        assertEquals(2, (result as ValidationResult.WrongPin).attemptsRemaining)
    }

    @Test
    fun `second wrong pin returns WrongPin with 1 remaining`() = runTest {
        coEvery { validationRepository.validatePin("v1", "9999") } returns Result.success(false)

        useCase("v1", "9999", "CHANGU CLUB", "2x1 Gin Tonic")
        val result = useCase("v1", "9999", "CHANGU CLUB", "2x1 Gin Tonic")

        assertTrue(result is ValidationResult.WrongPin)
        assertEquals(1, (result as ValidationResult.WrongPin).attemptsRemaining)
    }

    @Test
    fun `after 3 wrong pins returns Blocked for 30 seconds`() = runTest {
        coEvery { validationRepository.validatePin("v1", "9999") } returns Result.success(false)

        useCase("v1", "9999", "CHANGU CLUB", "2x1 Gin Tonic")
        useCase("v1", "9999", "CHANGU CLUB", "2x1 Gin Tonic")
        val result = useCase("v1", "9999", "CHANGU CLUB", "2x1 Gin Tonic")

        assertTrue(result is ValidationResult.Blocked)
        assertEquals(30, (result as ValidationResult.Blocked).unblockInSeconds)
    }

    @Test
    fun `correct pin resets attempt counter allowing new wrong pin sequence`() = runTest {
        coEvery { validationRepository.validatePin("v1", "9999") } returns Result.success(false)
        coEvery { validationRepository.validatePin("v1", "1234") } returns Result.success(true)
        coEvery { validationRepository.logValidation(any(), any(), any()) } returns Result.success(Unit)

        useCase("v1", "9999", "CHANGU CLUB", "2x1 Gin Tonic")
        useCase("v1", "1234", "CHANGU CLUB", "2x1 Gin Tonic")
        val afterReset = useCase("v1", "9999", "CHANGU CLUB", "2x1 Gin Tonic")

        assertTrue(afterReset is ValidationResult.WrongPin)
        assertEquals(2, (afterReset as ValidationResult.WrongPin).attemptsRemaining)
    }

    @Test
    fun `correct pin logs validation in repository`() = runTest {
        coEvery { validationRepository.validatePin("v1", "1234") } returns Result.success(true)
        coEvery { validationRepository.logValidation(any(), any(), any()) } returns Result.success(Unit)

        useCase("v1", "1234", "CHANGU CLUB", "2x1 Gin Tonic")

        coVerify(exactly = 1) {
            validationRepository.logValidation("v1", "CHANGU CLUB", "2x1 Gin Tonic")
        }
    }

    @Test
    fun `repository failure counts as wrong pin attempt`() = runTest {
        coEvery { validationRepository.validatePin("v1", "1234") } returns Result.failure(Exception("Network error"))

        val result = useCase("v1", "1234", "CHANGU CLUB", "2x1 Gin Tonic")

        assertTrue(result is ValidationResult.WrongPin)
    }
}

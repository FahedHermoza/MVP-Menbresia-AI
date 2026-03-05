package com.fahed.perupass

import com.fahed.perupass.domain.model.Venue
import com.fahed.perupass.domain.repository.VenueRepository
import com.fahed.perupass.domain.usecase.GetVenuesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetVenuesUseCaseTest {

    private val venueRepository: VenueRepository = mockk()
    private lateinit var useCase: GetVenuesUseCase

    @Before
    fun setUp() {
        useCase = GetVenuesUseCase(venueRepository)
    }

    @Test
    fun `invoke returns success result when repository succeeds`() = runTest {
        val venues = listOf(
            Venue(
                id = "v1", name = "CHANGU CLUB", address = "Cusco", latitude = -13.5,
                longitude = -71.9, imageUrl = "https://example.com/img.jpg", hours = "10PM",
                days = "Fri-Sat", rating = 4.8, isOpen = true, pin = "1234",
                benefits = mapOf("gold" to "Skip the Line")
            )
        )
        coEvery { venueRepository.getVenues() } returns Result.success(venues)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("CHANGU CLUB", result.getOrNull()?.first()?.name)
    }

    @Test
    fun `invoke returns failure result when repository fails`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { venueRepository.getVenues() } returns Result.failure(exception)

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke returns empty list when no venues`() = runTest {
        coEvery { venueRepository.getVenues() } returns Result.success(emptyList())

        val result = useCase()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `invoke calls repository exactly once`() = runTest {
        coEvery { venueRepository.getVenues() } returns Result.success(emptyList())

        useCase()

        coVerify(exactly = 1) { venueRepository.getVenues() }
    }
}

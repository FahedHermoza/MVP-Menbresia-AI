package com.fahed.perupass

import com.fahed.perupass.domain.model.Venue
import com.fahed.perupass.domain.repository.VenueRepository
import com.fahed.perupass.domain.usecase.GetVenueByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetVenueByIdUseCaseTest {

    private val venueRepository: VenueRepository = mockk()
    private lateinit var useCase: GetVenueByIdUseCase

    private val testVenue = Venue(
        id = "v1", name = "CHANGU CLUB", address = "Avenida El Sol 123, Cusco",
        latitude = -13.5, longitude = -71.9, imageUrl = "https://example.com/img.jpg",
        hours = "10PM — 4AM", days = "Thur–Sun", rating = 4.8, isOpen = true,
        pin = "1234", benefits = mapOf("gold" to "Skip the Line + 1 Free Drink")
    )

    @Before
    fun setUp() {
        useCase = GetVenueByIdUseCase(venueRepository)
    }

    @Test
    fun `invoke returns success when venue exists`() = runTest {
        coEvery { venueRepository.getVenueById("v1") } returns Result.success(testVenue)

        val result = useCase("v1")

        assertTrue(result.isSuccess)
        assertEquals("CHANGU CLUB", result.getOrNull()?.name)
    }

    @Test
    fun `invoke returns failure when venue not found`() = runTest {
        coEvery { venueRepository.getVenueById("missing") } returns
                Result.failure(NoSuchElementException("Venue not found: missing"))

        val result = useCase("missing")

        assertTrue(result.isFailure)
    }

    @Test
    fun `invoke calls repository with correct id`() = runTest {
        coEvery { venueRepository.getVenueById("v1") } returns Result.success(testVenue)

        useCase("v1")

        coVerify(exactly = 1) { venueRepository.getVenueById("v1") }
    }
}

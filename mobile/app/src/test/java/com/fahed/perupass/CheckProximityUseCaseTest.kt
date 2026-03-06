package com.fahed.perupass

import com.fahed.perupass.domain.usecase.CheckProximityUseCase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckProximityUseCaseTest {

    private lateinit var useCase: CheckProximityUseCase

    // CHANGU CLUB coordinates (Cusco)
    private val venueLat = -13.5179
    private val venueLng = -71.9780

    @Before
    fun setUp() {
        useCase = CheckProximityUseCase()
    }

    @Test
    fun `isInRange is true when user is within 50 meters`() {
        // ~10m north of the venue
        val userLat = venueLat + 0.00009
        val userLng = venueLng

        val result = useCase(userLat, userLng, venueLat, venueLng)

        assertTrue(result.isInRange)
        assertTrue(result.distanceMeters < 50f)
    }

    @Test
    fun `isInRange is false when user is more than 50 meters away`() {
        // ~200m north of the venue
        val userLat = venueLat + 0.0018
        val userLng = venueLng

        val result = useCase(userLat, userLng, venueLat, venueLng)

        assertFalse(result.isInRange)
        assertTrue(result.distanceMeters > 50f)
    }

    @Test
    fun `isInRange is false when user is exactly at 50 meters boundary`() {
        // ~50m north
        val userLat = venueLat + 0.00045
        val userLng = venueLng

        val result = useCase(userLat, userLng, venueLat, venueLng)

        // boundary is exclusive: distance < 50 → in range
        assertFalse(result.isInRange)
    }

    @Test
    fun `distanceMeters is zero when user is at the venue`() {
        val result = useCase(venueLat, venueLng, venueLat, venueLng)

        assertTrue(result.distanceMeters < 1f)
        assertTrue(result.isInRange)
    }
}

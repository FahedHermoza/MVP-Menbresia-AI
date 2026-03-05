package com.fahed.perupass

import com.fahed.perupass.data.model.dto.VenueDTO
import com.fahed.perupass.data.model.mapper.toDomain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VenueMapperTest {

    private val sampleDto = VenueDTO(
        id = "venue123",
        name = "CHANGU CLUB",
        address = "Avenida El Sol 123, Cusco",
        latitude = -13.5226,
        longitude = -71.9673,
        imageUrl = "https://images.unsplash.com/photo-1",
        hours = "10PM — 4AM",
        days = "Thur–Sun",
        rating = 4.8,
        isOpen = true,
        pin = "1234",
        benefits = mapOf(
            "discovery" to "Cover fee + welcome drink",
            "gold" to "Skip the Line + 1 Free Drink"
        )
    )

    @Test
    fun `toDomain maps all fields correctly`() {
        val venue = sampleDto.toDomain()

        assertEquals("venue123", venue.id)
        assertEquals("CHANGU CLUB", venue.name)
        assertEquals("Avenida El Sol 123, Cusco", venue.address)
        assertEquals(-13.5226, venue.latitude, 0.0001)
        assertEquals(-71.9673, venue.longitude, 0.0001)
        assertEquals("https://images.unsplash.com/photo-1", venue.imageUrl)
        assertEquals("10PM — 4AM", venue.hours)
        assertEquals("Thur–Sun", venue.days)
        assertEquals(4.8, venue.rating, 0.001)
        assertEquals("1234", venue.pin)
    }

    @Test
    fun `toDomain maps isOpen correctly when true`() {
        val venue = sampleDto.toDomain()
        assertTrue(venue.isOpen)
    }

    @Test
    fun `toDomain maps isOpen correctly when false`() {
        val venue = sampleDto.copy(isOpen = false).toDomain()
        assertFalse(venue.isOpen)
    }

    @Test
    fun `toDomain maps benefits map correctly`() {
        val venue = sampleDto.toDomain()

        assertEquals(2, venue.benefits.size)
        assertEquals("Cover fee + welcome drink", venue.benefits["discovery"])
        assertEquals("Skip the Line + 1 Free Drink", venue.benefits["gold"])
    }

    @Test
    fun `toDomain maps empty benefits to empty map`() {
        val venue = sampleDto.copy(benefits = emptyMap()).toDomain()
        assertTrue(venue.benefits.isEmpty())
    }
}

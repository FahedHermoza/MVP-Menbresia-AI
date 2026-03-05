package com.fahed.perupass.data.model.mapper

import com.fahed.perupass.data.model.dto.VenueDTO
import com.fahed.perupass.domain.model.Venue

fun VenueDTO.toDomain(): Venue = Venue(
    id = id,
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    imageUrl = imageUrl,
    hours = hours,
    days = days,
    rating = rating,
    isOpen = isOpen,
    pin = pin,
    benefits = benefits
)

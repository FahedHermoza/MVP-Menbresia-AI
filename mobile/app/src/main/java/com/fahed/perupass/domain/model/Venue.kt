package com.fahed.perupass.domain.model

data class Venue(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val hours: String,
    val days: String,
    val rating: Double,
    val isOpen: Boolean,
    val pin: String,
    val benefits: Map<String, String>
)

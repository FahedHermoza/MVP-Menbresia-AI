package com.fahed.perupass.data.model.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class VenueDTO(
    @DocumentId val id: String = "",
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl")
    var imageUrl: String = "",
    val hours: String = "",
    val days: String = "",
    val rating: Double = 0.0,
    @get:PropertyName("isOpen") @set:PropertyName("isOpen")
    var isOpen: Boolean = false,
    val pin: String = "",
    val benefits: Map<String, String> = emptyMap()
)

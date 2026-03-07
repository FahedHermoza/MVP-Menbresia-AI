package com.fahed.perupass.domain.model

data class PaymentRecord(
    val orderId: String,
    val plan: String,
    val amount: Double,
    val currency: String,
    val imageUrl: String,
    val status: String,
    val createdAt: Long
)

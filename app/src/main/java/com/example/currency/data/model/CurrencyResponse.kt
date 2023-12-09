package com.example.currency.data.model

data class CurrencyResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)

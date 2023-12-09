package com.example.currency.data.repository

import com.example.currency.network.CurrencyApiService
import com.example.currency.data.model.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(private val apiService: CurrencyApiService) {
    suspend fun getLatestRates(accessKey: String): ApiResponse {
        return apiService.getLatestRates(accessKey)
    }
}
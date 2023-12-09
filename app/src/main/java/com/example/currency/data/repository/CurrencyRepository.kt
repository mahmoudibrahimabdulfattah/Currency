package com.example.currency.data.repository

import com.example.currency.data.model.CurrencyResponse
import com.example.currency.network.CurrencyApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(private val apiService: CurrencyApi) {
    suspend fun getLatestRates(apiKey: String) = apiService.getLatestRates(apiKey)
}
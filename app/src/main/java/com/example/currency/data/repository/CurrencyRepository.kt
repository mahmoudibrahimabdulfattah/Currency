package com.example.currency.data.repository

import com.example.currency.data.model.CurrencyResponse
import com.example.currency.network.CurrencyApi
import retrofit2.Response
import javax.inject.Singleton

@Singleton
class CurrencyRepository(private val api: CurrencyApi) {

    suspend fun getLatestRates(): Response<CurrencyResponse> {
        return api.getLatestRates()
    }
}
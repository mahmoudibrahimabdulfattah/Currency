package com.example.currency.network

import com.example.currency.data.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("latest")
    suspend fun getLatestRates(@Query("access_key") apiKey: String): CurrencyResponse

}
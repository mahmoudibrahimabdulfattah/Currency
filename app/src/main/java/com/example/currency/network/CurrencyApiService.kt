package com.example.currency.network

import com.example.currency.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest")
    suspend fun getLatestRates(@Query("access_key") accessKey: String): ApiResponse
}
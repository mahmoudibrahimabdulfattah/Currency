package com.example.currency.network

import com.example.currency.data.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {
    @GET("latest?access_key=13cb1cf6fec19ea9aefff54aeffbd906")
    suspend fun getLatestRates(): Response<CurrencyResponse>
}
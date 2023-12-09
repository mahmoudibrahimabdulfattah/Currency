package com.example.currency.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.data.repository.CurrencyRepository
import com.example.currency.data.model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {
    private val _rates = MutableLiveData<ApiResponse>()
    val rates: LiveData<ApiResponse> get() = _rates
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchLatestRates(accessKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getLatestRates(accessKey)
                _rates.value = response
            } catch (e: IOException) {
                _error.value = "Internet connection error"
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred"
            }
        }
    }

    fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double? {
        val rates = _rates.value?.rates
        if (rates != null) {
            val fromRate = rates[fromCurrency]
            val toRate = rates[toCurrency]

            if (fromRate != null && toRate != null) {
                return amount * (toRate / fromRate)
            }
        }
        return null
    }
}
package com.example.currency.ui.fragments.convert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.data.repository.CurrencyRepository
import com.example.currency.data.model.CurrencyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

// CurrencyViewModel.kt
@HiltViewModel
class CurrencyViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {
    private val _rates = MutableLiveData<Map<String, Double>>()
    val rates: LiveData<Map<String, Double>> get() = _rates

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getLatestRates() {
        viewModelScope.launch {
            try {
                val response = repository.getLatestRates()
                if (response.isSuccessful && response.body()?.success == true) {
                    _rates.value = response.body()?.rates
                } else {
                    _error.value = "API Error: ${response.code()}"
                }
            } catch (e: IOException) {
                _error.value = "Network Error: ${e.message}"
            }
        }
    }

    fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double? {
        val rates = _rates.value
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
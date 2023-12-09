package com.example.currency.ui.fragments.convert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.data.repository.CurrencyRepository
import com.example.currency.data.model.CurrencyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {

    private val _currencyData = MutableLiveData<CurrencyResponse>()
    val currencyData: LiveData<CurrencyResponse> get() = _currencyData

    // Spinner selections
    var fromCurrency: String = ""
    var toCurrency: String = ""

    // Amount entered by the user
    var amount: Double = 0.0

    // Result of the currency conversion
    private val _conversionResult = MutableLiveData<Double>()
    val conversionResult: LiveData<Double> get() = _conversionResult

    // Error message
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Loading indicator
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // Function to fetch latest currency rates
    fun fetchLatestRates(apiKey: String) {
        _loading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getLatestRates(apiKey)
                _currencyData.postValue(response)

                if (response.success) {
                    _error.postValue("") // Clear previous error messages on success
                } else {
                    _error.postValue("Failed to fetch currency rates")
                }
            } catch (e: Exception) {
                _error.postValue("Failed to fetch currency rates")
            } finally {
                _loading.postValue(false)
            }
        }
    }

    // Function to convert currency
    fun convertCurrency() {
        _loading.postValue(true)

        val currencyRates = _currencyData.value?.rates

        if (currencyRates != null && fromCurrency.isNotEmpty() && toCurrency.isNotEmpty()) {
            val fromRate = currencyRates[fromCurrency] ?: 1.0
            val toRate = currencyRates[toCurrency] ?: 1.0

            val result = (amount / fromRate) * toRate
            _conversionResult.postValue(result)
        } else {
            _error.postValue("Invalid conversion parameters")
        }

        _loading.postValue(false)
    }
}
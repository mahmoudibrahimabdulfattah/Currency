package com.example.currency.ui.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.currency.databinding.FragmentCurrencyConverterBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment() {

    @Inject
    private lateinit var binding: FragmentCurrencyConverterBinding
    private lateinit var viewModel: CurrencyViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)

        val currencyList = listOf("USD", "EUR", "GBP", "JPY")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter

        viewModel.rates.observe(viewLifecycleOwner, { rates ->
            binding.textViewResult.text = convertCurrency(rates)
        })

        viewModel.error.observe(viewLifecycleOwner, { error ->
            // Handle error
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        })


        binding.buttonConvert.setOnClickListener {
            val amountText = binding.editTextAmount.text.toString()
            if (amountText.isNotEmpty()) {
                viewModel.getLatestRates()
            } else {
                //Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun convertCurrency(rates: Map<String, Double>?): String {
        val amountText = binding.editTextAmount.text.toString()
        val amount = amountText.toDoubleOrNull()

        return if (amount != null && rates != null) {
            val fromCurrency = binding.spinnerFrom.selectedItem.toString()
            val toCurrency = binding.spinnerTo.selectedItem.toString()

            val fromRate = rates[fromCurrency]
            val toRate = rates[toCurrency]

            if (fromRate != null && toRate != null) {
                val result = (amount / fromRate) * toRate
                String.format(Locale.getDefault(), "%.2f %s", result, toCurrency)
            } else {
                "Invalid currency codes"
            }
        } else {
            "Invalid amount or rates not available"
        }
    }
}
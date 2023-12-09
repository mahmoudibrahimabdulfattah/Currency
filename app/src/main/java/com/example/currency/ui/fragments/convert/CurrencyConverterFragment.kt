package com.example.currency.ui.fragments.convert

import android.R
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.currency.databinding.FragmentCurrencyConverterBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment() {

    private lateinit var binding: FragmentCurrencyConverterBinding
    private val viewModel: CurrencyViewModel by viewModels()


    private val currencyCodes = arrayOf("USD", "EUR", "EGP", "JPY", "CAD", "AUD", "CHF", "CNY", "SEK", "NZD")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()
        observeViewModel()
        viewModel.fetchLatestRates("13cb1cf6fec19ea9aefff54aeffbd906")

        setupUI()

        binding.buttonConvert.setOnClickListener {
            convertCurrency()
        }
    }

    private fun setupSpinners() {

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, currencyCodes)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter

        binding.spinnerFrom.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.fromCurrency = currencyCodes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (if needed)
            }
        })

        binding.spinnerTo.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.toCurrency = currencyCodes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (if needed)
            }
        })
    }

    private fun observeViewModel() {
        viewModel.currencyData.observe(viewLifecycleOwner) { currencyResponse ->
            // Update UI with currency data if needed
        }

        viewModel.conversionResult.observe(viewLifecycleOwner) { result ->
            binding.textViewResult.text = String.format("%.2f", result)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotBlank()) {
                showToast(errorMessage)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Show loading indicator (e.g., progress bar)
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupUI() {
        binding.buttonConvert.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        // Check if the network is available
        if (!isNetworkAvailable()) {
            showToast("No internet connection")
            return
        }

        // Check if both "from" and "to" currencies are selected
        if (viewModel.fromCurrency.isEmpty() || viewModel.toCurrency.isEmpty()) {
            showToast("Please select both 'from' and 'to' currencies")
            return
        }

        // Get the amount from the EditText
        val amountText = binding.editTextAmount.text.toString()
        if (amountText.isNotBlank()) {
            viewModel.amount = amountText.toDouble()
            viewModel.convertCurrency()
        } else {
            // Handle case where the amount is not entered
            showToast("Please enter an amount")
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
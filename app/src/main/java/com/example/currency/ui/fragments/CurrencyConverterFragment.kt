package com.example.currency.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.currency.databinding.FragmentCurrencyConverterBinding
import com.example.currency.ui.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment() {

    private lateinit var binding: FragmentCurrencyConverterBinding
    private val viewModel: CurrencyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnConvert.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toDoubleOrNull() ?: 0.0
            val fromCurrency = binding.spinnerFromCurrency.selectedItem.toString()
            val toCurrency = binding.spinnerToCurrency.selectedItem.toString()

            viewModel.convertCurrency(amount, fromCurrency, toCurrency)?.let { result ->
                binding.tvResult.text = String.format(Locale.getDefault(), "%.2f", result)
            } ?: run {
                binding.tvResult.text = ""
                binding.tvError.text = "Error converting currency"
            }
        }

        return binding.root
    }
}
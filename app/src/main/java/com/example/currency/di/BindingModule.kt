package com.example.currency.di

import com.example.currency.databinding.FragmentCurrencyConverterBinding
import com.example.currency.databinding.FragmentDetailsBinding
import com.example.currency.ui.fragments.convert.CurrencyConverterFragment
import com.example.currency.ui.fragments.details.DetailsFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object BindingModule {

    @Provides
    fun provideCurrencyConverterBinding(fragment: CurrencyConverterFragment): FragmentCurrencyConverterBinding {
        return FragmentCurrencyConverterBinding.bind(fragment.requireView())
    }

    @Provides
    fun provideDetailsBinding(fragment: DetailsFragment): FragmentDetailsBinding {
        return FragmentDetailsBinding.bind(fragment.requireView())
    }
}
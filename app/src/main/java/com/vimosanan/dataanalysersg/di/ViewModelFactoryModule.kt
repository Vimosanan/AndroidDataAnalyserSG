package com.vimosanan.dataanalysersg.di

import androidx.lifecycle.ViewModelProvider
import com.vimosanan.dataanalysersg.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(modelProvidersFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}
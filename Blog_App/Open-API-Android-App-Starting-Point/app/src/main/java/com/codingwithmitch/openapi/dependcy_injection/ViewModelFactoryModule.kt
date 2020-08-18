package com.codingwithmitch.openapi.dependcy_injection

import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory) : ViewModelProvider.Factory
}
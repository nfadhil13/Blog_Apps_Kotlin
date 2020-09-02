package com.codingwithmitch.openapi.dependcy_injection.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.dependcy_injection.auth.keys.AuthViewModelKey
import com.codingwithmitch.openapi.ui.auth.AuthViewModel
import com.codingwithmitch.openapi.viewmodels.AuthViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @AuthScope
    @Binds
    abstract fun bindAuthViewModelFactory(factory : AuthViewModelFactory) : ViewModelProvider.Factory

    @AuthScope
    @Binds
    @IntoMap
    @AuthViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel) : ViewModel
}
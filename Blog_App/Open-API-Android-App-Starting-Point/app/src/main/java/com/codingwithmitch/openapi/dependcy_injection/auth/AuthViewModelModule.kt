package com.codingwithmitch.openapi.dependcy_injection.auth

import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.dependcy_injection.ViewModelKey
import com.codingwithmitch.openapi.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel) : ViewModel
}
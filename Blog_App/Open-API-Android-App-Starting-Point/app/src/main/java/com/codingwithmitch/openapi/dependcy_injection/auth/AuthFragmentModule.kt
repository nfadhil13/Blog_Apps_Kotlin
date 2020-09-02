package com.codingwithmitch.openapi.dependcy_injection.auth

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.fragments.auth.AuthFragmentFactory
import dagger.Module
import dagger.Provides

@Module
object AuthFragmentModule {

    @JvmStatic
    @AuthScope
    @Provides
    fun provideAuthFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ) : FragmentFactory{
        return AuthFragmentFactory(
            viewModelFactory = viewModelFactory
        )
    }
}
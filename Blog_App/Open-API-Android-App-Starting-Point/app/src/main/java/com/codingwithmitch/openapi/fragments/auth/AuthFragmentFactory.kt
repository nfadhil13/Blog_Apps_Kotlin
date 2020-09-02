package com.codingwithmitch.openapi.fragments.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.dependcy_injection.auth.AuthScope
import com.codingwithmitch.openapi.ui.auth.ForgotPasswordFragment
import com.codingwithmitch.openapi.ui.auth.LauncherFragment
import com.codingwithmitch.openapi.ui.auth.LoginFragment
import com.codingwithmitch.openapi.ui.auth.RegisterFragment
import com.codingwithmitch.openapi.viewmodels.AuthViewModelFactory
import java.lang.Exception
import javax.inject.Inject

@AuthScope
class AuthFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        when (className) {
            LauncherFragment::class.java.name -> {
                return LauncherFragment(viewModelFactory)
            }
            LoginFragment::class.java.name -> {
                return LoginFragment(viewModelFactory)
            }
            RegisterFragment::class.java.name -> {
                return RegisterFragment(viewModelFactory)
            }
            ForgotPasswordFragment::class.java.name -> {
                return ForgotPasswordFragment(viewModelFactory)
            }
            else ->
                throw Exception("UNKNOWN FRAGMENT")
        }
    }
}


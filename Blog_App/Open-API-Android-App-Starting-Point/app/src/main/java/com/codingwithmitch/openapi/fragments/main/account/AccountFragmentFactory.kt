package com.codingwithmitch.openapi.fragments.main.account

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.dependcy_injection.main.MainScope
import com.codingwithmitch.openapi.ui.auth.LauncherFragment
import com.codingwithmitch.openapi.ui.main.account.AccountFragment
import com.codingwithmitch.openapi.ui.main.account.ChangePasswordFragment
import com.codingwithmitch.openapi.ui.main.account.UpdateAccountFragment
import java.lang.Exception
import javax.inject.Inject

@MainScope
class AccountFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        when (className) {
            AccountFragment::class.java.name -> {
                return AccountFragment(viewModelFactory)
            }
            ChangePasswordFragment::class.java.name -> {
                return ChangePasswordFragment(viewModelFactory)
            }
            UpdateAccountFragment::class.java.name -> {
                return UpdateAccountFragment(viewModelFactory)
            }
            else ->
                throw Exception("UNKNOWN FRAGMENT")
        }
    }
}


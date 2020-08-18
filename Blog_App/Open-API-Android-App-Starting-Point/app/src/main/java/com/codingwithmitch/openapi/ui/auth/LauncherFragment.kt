package com.codingwithmitch.openapi.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.session.SessionManager
import kotlinx.android.synthetic.main.fragment_launcher.*
import javax.inject.Inject


class LauncherFragment :  BaseAuthFragment()  {
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("DEBUG : ${viewModel.hashCode()}")
        setListener()

//        focusable_view.requestFocus()
    }

    private fun setListener(){
        tv_register.setOnClickListener {
            navRegistration()
        }

        tv_login.setOnClickListener{
            navLogin()
        }

        tv_forgot_password.setOnClickListener {
            navForgotPassword()
        }
        focusable_view.requestFocus()
    }

    private fun navForgotPassword() {
        findNavController().navigate(R.id.action_launcherFragment_to_forgotPasswordFragment)
    }

    private fun navLogin() {
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    private fun navRegistration() {
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)
    }
}
package com.codingwithmitch.openapi.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.FragmentLoginBinding
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.ui.auth.state.AuthStateEvent
import com.codingwithmitch.openapi.ui.auth.state.AuthStateEvent.LoginAttemptEvent
import com.codingwithmitch.openapi.ui.auth.state.LogInFields
import com.codingwithmitch.openapi.util.GenericApiResponse
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment :  BaseAuthFragment()  {

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("DEBUG : ${viewModel.hashCode()}")

        subscribeObservers()

        subscribeListener()

    }

    private fun subscribeListener() {
        binding.apply {
            loginButton.setOnClickListener {
                login()
            }
        }
    }

    private fun login(){
        viewModel.setStateEvent(LoginAttemptEvent(
            email = binding.inputEmail.text.toString(),
            password =  binding.inputPassword.text.toString()
        ))
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.logInField?.let {loginFiled->
                loginFiled.login_email?.let{email -> binding.inputEmail.setText(email)}
                loginFiled.login_password?.let{password -> binding.inputPassword.setText(password)}
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLogInFields(
            LogInFields(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString())
        )
    }
}
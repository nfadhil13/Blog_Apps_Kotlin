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
import com.codingwithmitch.openapi.databinding.FragmentRegisterBinding
import com.codingwithmitch.openapi.ui.auth.state.AuthStateEvent
import com.codingwithmitch.openapi.ui.auth.state.RegistrationFields
import com.codingwithmitch.openapi.util.GenericApiResponse

class RegisterFragment : BaseAuthFragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObserver()
        subscribeListener()
    }

    private fun subscribeListener() {
        binding.apply {
            registerButton.setOnClickListener {
                register()
            }
        }
    }

    private fun register() {
        viewModel.setStateEvent(
            AuthStateEvent.RegisterAttemptEvent(
                email =binding.inputEmail.text.toString(),
                username = binding.inputUsername.text.toString(),
                password = binding.inputPassword.text.toString(),
                confirm_password = binding.inputPasswordConfirm.text.toString()
            )
        )
    }

    private fun subscribeObserver() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.registrationFields?.let { registrationFields ->

                println("observing")

                //Set The Registration TextField
                registrationFields.registration_email
                    ?.let { email -> binding.inputEmail.setText(email) }

                //Set The Registration Username
                registrationFields.registration_username
                    ?.let { username -> binding.inputUsername.setText(username) }

                //Set the registration Password
                registrationFields.registration_password
                    ?.let { password -> binding.inputPassword.setText(password) }

                //Set the registration confirm password
                registrationFields.registration_confirm_password
                    ?.let { confirmPassword -> binding.inputPasswordConfirm.setText(confirmPassword) }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        val registration = RegistrationFields()
        println("destroy")
        viewModel.setRegistrationFields(
            registration.apply {
                registration_email = binding.inputEmail.text.toString()
                registration_username = binding.inputUsername.text.toString()
                registration_password = binding.inputPassword.text.toString()
                registration_confirm_password = binding.inputPasswordConfirm.text.toString()
            }
        )
    }
}
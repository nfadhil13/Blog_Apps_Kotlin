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
import java.lang.Exception

class RegisterFragment : BaseAuthFragment() {

    private var binding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding?.root ?: throw Exception("Fail to get the binding")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObserver()
        subscribeListener()
    }

    private fun subscribeListener() {
        binding!!.apply {
            registerButton.setOnClickListener {
                register()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.let { it ->
            viewModel.setRegistrationFields(
                RegistrationFields(
                    registration_email = it.inputEmail.text.toString(),
                    registration_password = it.inputPassword.text.toString(),
                    registration_confirm_password = it.inputPasswordConfirm.text.toString(),
                    registration_username = it.inputUsername.text.toString()
                )
            )
            binding = null
        }
    }

    private fun register() {
        binding?.let { nonNullBinding ->
            viewModel.setStateEvent(
                AuthStateEvent.RegisterAttemptEvent(
                    email = nonNullBinding.inputEmail.text.toString(),
                    username = nonNullBinding.inputUsername.text.toString(),
                    password = nonNullBinding.inputPassword.text.toString(),
                    confirm_password = nonNullBinding.inputPasswordConfirm.text.toString()
                )
            )


        }
    }

    private fun subscribeObserver() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.registrationFields?.let { registrationFields ->
                binding?.let { nonNullBinding ->
                    println("observing")

                    //Set The Registration TextField
                    registrationFields.registration_email
                        ?.let { email -> nonNullBinding.inputEmail.setText(email) }

                    //Set The Registration Username
                    registrationFields.registration_username
                        ?.let { username -> nonNullBinding.inputUsername.setText(username) }

                    //Set the registration Password
                    registrationFields.registration_password
                        ?.let { password -> nonNullBinding.inputPassword.setText(password) }

                    //Set the registration confirm password
                    registrationFields.registration_confirm_password
                        ?.let { confirmPassword ->
                            nonNullBinding.inputPasswordConfirm.setText(confirmPassword)
                        }
                }
            }
        })
    }



}
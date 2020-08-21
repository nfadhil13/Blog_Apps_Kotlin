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
import java.lang.Exception


class LoginFragment :  BaseAuthFragment()  {

    private var binding : FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        return binding?.root ?: throw Exception("Fail to inflate the binding")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("DEBUG : ${viewModel.hashCode()}")

        subscribeObservers()

        subscribeListener()

    }

    private fun subscribeListener() {
        binding?.apply {
            loginButton.setOnClickListener {
                login()
            }
        }
    }

    private fun login(){
        binding?.let{nonNullBinding->
            viewModel.setStateEvent(LoginAttemptEvent(
                email = nonNullBinding.inputEmail.text.toString(),
                password =  nonNullBinding.inputPassword.text.toString()
            ))
        }
        Log.d(TAG,"Try to log in with : ${binding!!.inputEmail.text} and ${binding!!.inputPassword.text}")
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            binding?.let{nonNullBinding->
                viewState.logInField?.let {loginFiled->
                    loginFiled.login_email?.let{email -> nonNullBinding.inputEmail.setText(email)}
                    loginFiled.login_password?.let{password -> nonNullBinding.inputPassword.setText(password)}
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.let {nonNullBInding->
            viewModel.setLogInFields(
                LogInFields(
                    nonNullBInding.inputEmail.text.toString(),
                    nonNullBInding.inputPassword.text.toString())
            )
            binding = null
        }

    }

}
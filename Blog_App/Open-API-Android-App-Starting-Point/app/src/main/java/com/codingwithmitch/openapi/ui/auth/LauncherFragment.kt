package com.codingwithmitch.openapi.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import kotlinx.android.synthetic.main.fragment_launcher.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LauncherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LauncherFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        findNavController().navigate(R.id.registerFragment)
    }
}
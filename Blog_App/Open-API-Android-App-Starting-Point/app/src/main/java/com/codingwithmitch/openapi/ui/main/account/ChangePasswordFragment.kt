package com.codingwithmitch.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.FragmentChangePasswordBinding
import com.codingwithmitch.openapi.ui.main.account.state.AccountStateEvent
import com.codingwithmitch.openapi.util.SuccessHandler.Companion.RESPONSE_PASSWORD_UPDATE_SUCCESS

class ChangePasswordFragment : BaseAccountFragment(){

    private  var binding : FragmentChangePasswordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_change_password, container, false)
        return binding?.root ?: throw Exception("Error")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            this.updatePasswordButton.setOnClickListener {
                Log.d(TAG,"ChangePasswordFragment : Changing Password Button")
                viewModel.setStateEvent(
                    AccountStateEvent.ChangePasswordEvent(
                        this.inputCurrentPassword.text.toString(),
                        this.inputNewPassword.text.toString(),
                        this.inputConfirmNewPassword.text.toString()
                    )
                )
            }
            println(this.inputConfirmNewPassword.text.toString())
        }

        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {dataState->
            stateChangeListener.onDataStateChange(dataState)
            Log.d(TAG,"ChangePasswordFragment , DataState : $dataState")
            dataState?.let{nonNullDataState->
                nonNullDataState.data?.let {data->
                    data.response?.let{event->
                        if(event.peekContent().message
                                .equals(RESPONSE_PASSWORD_UPDATE_SUCCESS)){
                            stateChangeListener.hideSoftKeyboard()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
            
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
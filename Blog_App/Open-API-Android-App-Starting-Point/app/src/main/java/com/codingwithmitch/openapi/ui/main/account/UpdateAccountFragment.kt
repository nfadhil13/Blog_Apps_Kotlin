package com.codingwithmitch.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.FragmentUpdateAccountBinding
import com.codingwithmitch.openapi.models.AccountProperties
import com.codingwithmitch.openapi.ui.main.account.state.AccountStateEvent

class UpdateAccountFragment : BaseAccountFragment(){

    private  var binding : FragmentUpdateAccountBinding? =null

    private val EMAIL_KEY = "email"
    private val USERNAME_KEY = "username"

    private var isFirstTime = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_update_account,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        savedInstanceState?.let{it ->
            isFirstTime = false
            val email = it.getString(EMAIL_KEY) ?: ""
            val username = it.getString(USERNAME_KEY) ?: ""
            setAccountDataFields(email,username)
        }
        subscribeObserver()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save ->{
                saveChanges()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "UpdateAccountFragment : onDestroyView : Make binding instance null")

        binding = null
    }

    private fun subscribeObserver(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer {dataState->
            stateChangeListener.onDataStateChange(dataState)
        })

        if(isFirstTime){
            viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
                viewState?.let{
                    it.accountProperties?.let{accountProperties->
                        setAccountDataFields(
                            email = accountProperties.email,
                            username = accountProperties.username
                        )
                    }
                }
            })
        }
    }

    private fun setAccountDataFields(email : String , username : String) {
        binding?.apply {
            this.inputEmail.setText(email)
            this.inputUsername.setText(username)
        }
    }

    private fun saveChanges(){
        binding?.apply{
            viewModel.setStateEvent(
                AccountStateEvent.UpdateAccountPropertiesEvent(
                    this.inputEmail.text.toString(),
                    this.inputUsername.text.toString()
                )
            )
            stateChangeListener.hideSoftKeyboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.apply {
            bundleOf(
                EMAIL_KEY to this.inputEmail.text.toString(),
                USERNAME_KEY to this.inputUsername.text.toString()
            )
        }
    }
}
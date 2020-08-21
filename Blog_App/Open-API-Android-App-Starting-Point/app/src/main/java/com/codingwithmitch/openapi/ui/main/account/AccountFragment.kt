package com.codingwithmitch.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.FragmentAccountBinding
import com.codingwithmitch.openapi.models.AccountProperties
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.main.account.state.AccountStateEvent
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject

class AccountFragment : BaseAccountFragment() {

    private var binding: FragmentAccountBinding? =null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        subscribeObservers()
        return binding?.root ?: throw Exception("Error Binding")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding?.apply {
            changePassword.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
            }

            logoutButton.setOnClickListener {
                viewModel.logOut()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(
            AccountStateEvent.GetAccountPropertiesEvent()
        )

    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState?.let{
                it.data?.let{data->
                    data.data?.let{event->
                        event.getContentIfNotHandled()?.let{viewState->
                            viewState.accountProperties?.let{accountProperties ->
                                Log.d(TAG, "AccountFragment : Datastate changing $it")
                                viewModel.setAccountPropertiesData(accountProperties)
                            }
                        }
                    }
                }
            }
        })
    }
    private fun setAccountDataFields(accountProperties: AccountProperties){
        binding?.apply {
            this.email.setText(accountProperties.email)
            this.username.setText(accountProperties.username)
        }
    }
}
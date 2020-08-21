package com.codingwithmitch.openapi.ui.main.account

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import com.codingwithmitch.openapi.models.AccountProperties
import com.codingwithmitch.openapi.repository.main.AccountRepository
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.BaseViewModel
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.main.account.state.AccountStateEvent
import com.codingwithmitch.openapi.ui.main.account.state.AccountStateEvent.GetAccountPropertiesEvent
import com.codingwithmitch.openapi.ui.main.account.state.AccountStateEvent.UpdateAccountPropertiesEvent
import com.codingwithmitch.openapi.ui.main.account.state.AccountViewState
import com.codingwithmitch.openapi.util.AbsenLiveData
import javax.inject.Inject

class AccountViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    val accountRepository: AccountRepository
) : BaseViewModel<AccountStateEvent , AccountViewState>() {
    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }


    override fun handleStateEvent(stateEvent: AccountStateEvent): LiveData<DataState<AccountViewState>> {
        when(stateEvent){
            is GetAccountPropertiesEvent -> {
                return sessionManager.cachedToken.value?.let {authToken ->
                    accountRepository.getAccountProperties(authToken)
                }?: AbsenLiveData.create()
            }

            is UpdateAccountPropertiesEvent->{
                return AbsenLiveData.create()
            }

            is AccountStateEvent.ChangePasswordEvent -> {
                return AbsenLiveData.create()
            }
        }
    }

    fun setAccountPropertiesData(accountProperties: AccountProperties){
        val update = getCurrentViewStateOrNew()
        if(update.accountProperties == accountProperties){
                return
        }
        update.accountProperties = accountProperties
        _viewState.value = update
    }

    fun logOut(){
        Log.d(TAG,"AccountViewModel : LogingOut")
        sessionManager.logout()
    }
}
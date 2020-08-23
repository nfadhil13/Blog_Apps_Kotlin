package com.codingwithmitch.openapi.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.repository.auth.AuthRepository
import com.codingwithmitch.openapi.ui.BaseViewModel
import com.codingwithmitch.openapi.ui.Data
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.auth.state.AuthStateEvent
import com.codingwithmitch.openapi.ui.auth.state.AuthStateEvent.*
import com.codingwithmitch.openapi.ui.auth.state.AuthViewState
import com.codingwithmitch.openapi.ui.auth.state.LogInFields
import com.codingwithmitch.openapi.ui.auth.state.RegistrationFields
import com.codingwithmitch.openapi.util.AbsenLiveData
import javax.inject.Inject


class AuthViewModel
@Inject
constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<AuthStateEvent, AuthViewState>() {

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }


    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when (stateEvent) {

            is LoginAttemptEvent -> {
                return authRepository.attemptLogin(
                    stateEvent.email,
                    stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {
                return authRepository.attemptRegister(
                    stateEvent.email,
                    stateEvent.username,
                    stateEvent.password,
                    stateEvent.confirm_password
                )
            }

            is CheckPrevioustAuthEvent -> {
                Log.i(TAG,"AuthViewModel : EventHandle : Checking Previous AuthUser")
                return authRepository.checkPreviousAuthUser()
            }

            is None -> {
                return liveData<DataState<AuthViewState>> {
                    emit(DataState.data(null,null))
                }
            }
        }
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if(update.registrationFields == registrationFields)
            return
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLogInFields(logInFields: LogInFields){
        val update = getCurrentViewStateOrNew()
        if(update.logInField == logInFields)
            return
        update.logInField = logInFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken)
            return
        update.authToken = authToken
        _viewState.value = update
    }
    fun cancelActiveJobs(){
        handlePendingData()
        authRepository.cancelAllJobs()
    }

    fun handlePendingData(){
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }



}
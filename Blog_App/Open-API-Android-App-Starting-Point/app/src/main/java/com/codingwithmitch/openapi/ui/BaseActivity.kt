package com.codingwithmitch.openapi.ui

import android.util.Log
import com.codingwithmitch.openapi.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), DataStateChangeListener{

    val TAG = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onDataStateChange(dataState: DataState<*>?) {
        dataState?.let{dataState->
            GlobalScope.launch(Main){
                displayProgressBar(dataState.loading.isLoading)

                dataState.error?.let{errorEvent->
                    handleStateError(errorEvent)
                }

                dataState.data?.let {data->
                    data?.response.let {response->
                        handleStateResponse(response)
                    }
                }
            }
        }
    }

    private fun handleStateResponse(response: Event<Response>?){
        
    }

    private fun handleStateError(errorEvent: Event<StateError>) {
        errorEvent.getContentIfNotHandled()?.let {errorState->
            when(errorState.response.responseType){
                is ResponseType.Dialog ->{
                    errorState.response.message?.let{ errorMessage ->
                        displayErrorDialog(errorMessage)
                    }
                }
                is ResponseType.Toast->{
                    errorState.response.message?.let{errorMessage ->
                        displayShortToast(errorMessage)
                    }
                }
                is ResponseType.None->{
                    Log.e(TAG,"handleStateError : None Response Type ${errorState.response.message}")
                }
            }
        }
    }

    abstract fun displayProgressBar(showProgressBar : Boolean)
}
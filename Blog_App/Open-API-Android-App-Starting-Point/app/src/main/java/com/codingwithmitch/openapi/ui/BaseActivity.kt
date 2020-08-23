package com.codingwithmitch.openapi.ui

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
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
        dataState?.let{it->
            GlobalScope.launch(Main){
                Log.d(TAG,"START LOADING THE PROGRESS BAR : ${it.loading.isLoading}")
                displayProgressBar(it.loading.isLoading)

                it.error?.let{errorEvent->
                    handleStateError(errorEvent)
                }

                it.data?.let {data->
                    data.response?.let {response->
                        handleStateResponse(response)
                    }
                }
            }
        }
    }

    private fun handleStateResponse(response: Event<Response>?){
        response?.getContentIfNotHandled()?.let {it->
            when(it.responseType){
                is ResponseType.Dialog ->{
                    it.message?.let{ errorMessage ->
                        displayErrorDialog(errorMessage)
                    }
                }
                is ResponseType.Toast->{
                    it.message?.let{errorMessage ->
                        displayShortToast(errorMessage)
                    }
                }
                is ResponseType.None->{
                    Log.e(TAG,"handleStateResponse: None Response Type ${it.message}")
                }
            }
        }
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

    override fun hideSoftKeyboard() {
        currentFocus?.let{
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
        }
    }

    abstract fun displayProgressBar(showProgressBar : Boolean)
}
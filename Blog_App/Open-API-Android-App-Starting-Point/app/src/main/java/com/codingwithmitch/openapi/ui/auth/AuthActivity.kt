package com.codingwithmitch.openapi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.ActivityAuthBinding
import com.codingwithmitch.openapi.ui.BaseActivity
import com.codingwithmitch.openapi.ui.Event
import com.codingwithmitch.openapi.ui.Response
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.auth.state.AuthStateEvent
import com.codingwithmitch.openapi.ui.main.MainActivity
import com.codingwithmitch.openapi.util.SuccessHandler
import com.codingwithmitch.openapi.util.SuccessHandler.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    private var binding: ActivityAuthBinding? =null

    @Inject
    lateinit var viewModolProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        viewModel = ViewModelProvider(this, viewModolProviderFactory).get(AuthViewModel::class.java)
        subscribeObservers()

    }

    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
    }

    fun checkPreviousAuthUser() {
        viewModel.setStateEvent(AuthStateEvent.CheckPrevioustAuthEvent())
    }

    fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->


            Log.i(TAG, "AuthActivity dataState $dataState")

            onDataStateChange(dataState)
            dataState.data?.let { data ->
                //Checking if the data is not nul then do something
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let {
                            Log.i(TAG, "AuthActivity data exist , Loging in")
                            viewModel.setAuthToken(it)
                        }
                    }
                }
                data.response?.let { event ->
                    event.peekContent().let{response ->
                        response.message?.let{message->
                            if(message.equals(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE)){
                                onFinishCheckPreviousAuthUser()
                            }
                        }
                    }
                }
            }


        })

        viewModel.viewState.observe(this, Observer {
            it.authToken?.let {authToken ->
                Log.d(
                    TAG,
                    "AuthActivity : SubscriberObservers : ViewState LoggingIn with AuthToken : $it"
                )
                authToken.token?.let{ sessionManager.login(authToken) }

            }
        })

        sessionManager.cachedToken.observe(this, Observer { dataState ->
            Log.d(TAG, "AuthActivity : SubscriberObservers : ChachedToken : DataState : $dataState")
            dataState.let{authToken->
                if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                    Log.d(TAG, "AuthActivity : SubscriberObservers : ChachedToken : Logging In $authToken")
                    navMainActivity()
                }
            }
        })
    }

    private fun onFinishCheckPreviousAuthUser() {
        binding?.let{
            it.fragmentContainer.visibility = View.VISIBLE
        }
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun displayProgressBar(showProgressBar: Boolean) {
        binding?.let{nonNullBinding->
            if (showProgressBar) {
                nonNullBinding.progressBar.visibility = View.VISIBLE
                Log.d(TAG, "AuthActivity : Showing ProgressBar")
            } else {
                nonNullBinding.progressBar.visibility = View.INVISIBLE
                Log.d(TAG, "AuthActivity : Hiding ProgressBar")
            }
        }

    }
}
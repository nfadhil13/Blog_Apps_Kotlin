package com.codingwithmitch.openapi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.BaseActivity
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.main.MainActivity
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var viewModolProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, viewModolProviderFactory).get(AuthViewModel::class.java)

        subscribeObservers()
    }

    fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->


            Log.i(TAG, "AuthActivity response id ${dataState.data==null}")

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
                Log.i(TAG, "AuthActivity response is null ${data.response==null}")
                data.response?.let { event ->
                    Log.d(TAG, "Authactivity error Dialog , there is an error")
                    event.getContentIfNotHandled()?.let {
                        when (it.responseType) {
                            is ResponseType.Dialog -> {
                                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            }
                            is ResponseType.Toast -> {
                                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            }

                            is ResponseType.None -> {

                            }
                        }
                    }
                }
            }


        })

        viewModel.viewState.observe(this, Observer {
            it.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, Observer { authToken ->
            Log.d(TAG, "AuthActivity : SubscriberObservers : AuthToken : $authToken")
            if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                navMainActivity()
            }
        })
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
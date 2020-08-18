package com.codingwithmitch.openapi.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.ActivityMainBinding
import com.codingwithmitch.openapi.ui.BaseActivity
import com.codingwithmitch.openapi.ui.auth.AuthActivity

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        subscribeObservers()
    }

    fun subscribeObservers(){
        sessionManager.cachedToken.observe(this, Observer { authToken->
            Log.d(TAG , "Mainactivity : SubscriberObservers : AuthToken : $authToken")
            if(authToken == null || authToken.account_pk == -1 || authToken.token == null){
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity() {
        val intent = Intent(this,AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.codingwithmitch.openapi

import android.app.Application
import com.codingwithmitch.openapi.dependcy_injection.AppComponent
import com.codingwithmitch.openapi.dependcy_injection.DaggerAppComponent
import com.codingwithmitch.openapi.dependcy_injection.auth.AuthComponent
import com.codingwithmitch.openapi.dependcy_injection.main.MainComponent

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    private var authComponent : AuthComponent? = null

    private var mainComponent : MainComponent? = null

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    fun authComponent() : AuthComponent {
        if(authComponent == null){
            authComponent = appComponent.authComponent().create()
        }
        return authComponent as AuthComponent
    }

    fun mainComponent() : MainComponent{
        if(mainComponent == null){
            mainComponent = appComponent.mainComponent().create()
        }
        return mainComponent as MainComponent
    }

    fun releaseAuthComponent(){
        authComponent = null
    }

    fun initAppComponent(){
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    fun relaseMainComponent() {
        mainComponent = null
    }


}
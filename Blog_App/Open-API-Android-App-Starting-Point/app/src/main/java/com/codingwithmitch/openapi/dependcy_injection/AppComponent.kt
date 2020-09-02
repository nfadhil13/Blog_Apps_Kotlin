package com.codingwithmitch.openapi.dependcy_injection

import android.app.Application
import com.codingwithmitch.openapi.BaseApplication
import com.codingwithmitch.openapi.dependcy_injection.auth.AuthComponent
import com.codingwithmitch.openapi.dependcy_injection.main.MainComponent
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.BaseActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentModule::class
    ]
)
interface AppComponent{


    val sessionManager : SessionManager

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application) : Builder
        fun build() : AppComponent
    }

    fun inject(baseActivity: BaseActivity)

    fun authComponent() : AuthComponent.Factory

    fun mainComponent() : MainComponent.Factory
}
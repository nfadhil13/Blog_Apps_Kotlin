package com.codingwithmitch.openapi.dependcy_injection

import com.codingwithmitch.openapi.dependcy_injection.auth.AuthFragmentBuildersModule
import com.codingwithmitch.openapi.dependcy_injection.auth.AuthModule
import com.codingwithmitch.openapi.dependcy_injection.auth.AuthScope
import com.codingwithmitch.openapi.dependcy_injection.auth.AuthViewModelModule
import com.codingwithmitch.openapi.dependcy_injection.main.MainFragmentBuildersModule
import com.codingwithmitch.openapi.dependcy_injection.main.MainModule
import com.codingwithmitch.openapi.dependcy_injection.main.MainScope
import com.codingwithmitch.openapi.dependcy_injection.main.MainViewModelModule
import com.codingwithmitch.openapi.ui.auth.AuthActivity
import com.codingwithmitch.openapi.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthModule::class,
            AuthFragmentBuildersModule::class,
            AuthViewModelModule::class
        ]
    )
    abstract fun contributeAuthActivity() : AuthActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentBuildersModule::class,
            MainModule::class,
            MainViewModelModule::class
        ]
    )
    abstract fun contributeMainActivity() : MainActivity
}
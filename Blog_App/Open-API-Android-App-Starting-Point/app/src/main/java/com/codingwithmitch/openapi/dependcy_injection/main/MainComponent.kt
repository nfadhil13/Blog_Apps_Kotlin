package com.codingwithmitch.openapi.dependcy_injection.main



import com.codingwithmitch.openapi.ui.main.MainActivity
import dagger.Subcomponent

@MainScope
@Subcomponent(
    modules = [
        MainModule::class,
        MainViewModelModule::class,
        MainFragmentModule::class
    ]
)
interface MainComponent{


    @Subcomponent.Factory
    interface Factory{
        fun create() : MainComponent
    }

    fun inject(mainActivity: MainActivity)


}
package com.codingwithmitch.openapi.dependcy_injection.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.dependcy_injection.auth.keys.AuthViewModelKey
import com.codingwithmitch.openapi.dependcy_injection.auth.keys.MainViewModelKey
import com.codingwithmitch.openapi.ui.main.account.AccountViewModel
import com.codingwithmitch.openapi.ui.main.blog.viewmodels.BlogViewModel
import com.codingwithmitch.openapi.viewmodels.MainViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @MainScope
    @Binds
    abstract fun provideMainViewModelFactory(factory : MainViewModelFactory) : ViewModelProvider.Factory

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel) : ViewModel

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel) : ViewModel


}
package com.codingwithmitch.openapi.dependcy_injection.main

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.fragments.auth.AuthFragmentFactory
import com.codingwithmitch.openapi.fragments.main.account.AccountFragmentFactory
import com.codingwithmitch.openapi.fragments.main.blog.BlogFragmentFactory
import com.codingwithmitch.openapi.fragments.main.create_blog.CreateBlogFragmentFactory
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Named

@Module
object MainFragmentModule {


    @JvmStatic
    @MainScope
    @Provides
    @Named("AccountFragmentFactory")
    fun provideAccountFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory,
        requestManager: RequestManager
    ) : FragmentFactory{
        return AccountFragmentFactory(
            viewModelFactory = viewModelFactory
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named("BlogFragmentFactory")
    fun provideBlogFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory,
        requestManager: RequestManager
    ) : FragmentFactory{
        return BlogFragmentFactory(
            viewModelFactory = viewModelFactory,
            requestManager = requestManager
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named("CreateBlogFragmentFactory")
    fun provideCreateBlogFragmentFactory() : FragmentFactory{
        return CreateBlogFragmentFactory()
    }
}
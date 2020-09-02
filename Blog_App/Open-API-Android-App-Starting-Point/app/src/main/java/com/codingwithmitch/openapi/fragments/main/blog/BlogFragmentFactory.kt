package com.codingwithmitch.openapi.fragments.main.blog

import android.util.Log
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.ui.main.blog.BlogFragment
import com.codingwithmitch.openapi.ui.main.blog.UpdateBlogFragment
import com.codingwithmitch.openapi.ui.main.blog.ViewBlogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.dependcy_injection.main.MainScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.Exception
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@MainScope
class BlogFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
) : FragmentFactory() {
    val TAG = "AppDebug"



    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        Log.d(TAG,"Class Blog : IntiateFragment : $className")
        when (className) {
            BlogFragment::class.java.name -> {
                return BlogFragment(viewModelFactory,requestManager)
            }
            ViewBlogFragment::class.java.name -> {
                return ViewBlogFragment(viewModelFactory,requestManager)
            }
            UpdateBlogFragment::class.java.name -> {
                return UpdateBlogFragment(viewModelFactory,requestManager)
            }
            else ->
                throw Exception("UNKNOWN FRAGMENT")
        }
    }
}


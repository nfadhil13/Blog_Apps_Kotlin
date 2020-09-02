package com.codingwithmitch.openapi.fragments.main.create_blog

import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.ui.main.blog.BlogFragment
import com.codingwithmitch.openapi.ui.main.blog.UpdateBlogFragment
import com.codingwithmitch.openapi.ui.main.blog.ViewBlogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.dependcy_injection.main.MainScope
import com.codingwithmitch.openapi.ui.main.create_blog.CreateBlogFragment
import java.lang.Exception
import javax.inject.Inject

@MainScope
class CreateBlogFragmentFactory
@Inject
constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        when (className) {
            CreateBlogFragment::class.java.name -> {
                return CreateBlogFragment()
            }
            else ->
                throw Exception("UNKNOWN FRAGMENT")
        }
    }
}


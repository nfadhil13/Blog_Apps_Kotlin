package com.codingwithmitch.openapi.ui.main.create_blog

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment


abstract class BaseCreateBlogFragment  : Fragment() {

    val TAG: String = "AppDebug"


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    abstract fun cancelActiveJobs()
}
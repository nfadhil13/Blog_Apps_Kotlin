package com.codingwithmitch.openapi.ui.main.blog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.DataStateChangeListener
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import java.lang.ClassCastException
import java.lang.Exception
import javax.inject.Inject

abstract class BaseBlogFragment : DaggerFragment(){
    val TAG = "AppDebug"

    lateinit var stateChangeListener: DataStateChangeListener

    @Inject
    lateinit var providerFactory : ViewModelProviderFactory

    lateinit var viewModel: BlogViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController(R.id.blogFragment , activity as AppCompatActivity)
        viewModel = activity?.run{
            ViewModelProvider(this,providerFactory).get(BlogViewModel::class.java)
        }?: throw Exception("Invalid Activity")
    }

    fun setUpActionBarWithNavController(fragmentId : Int , activity : AppCompatActivity){
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch (e : ClassCastException){
            Log.e(TAG ,"$context must implement stateChangeListener")
        }
    }

    fun cancelActiveJobs() {
        viewModel.cancelActiveJob()
    }
}
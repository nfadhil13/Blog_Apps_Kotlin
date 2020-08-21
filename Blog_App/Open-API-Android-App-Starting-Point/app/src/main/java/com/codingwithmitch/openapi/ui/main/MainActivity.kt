package com.codingwithmitch.openapi.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.ActivityMainBinding
import com.codingwithmitch.openapi.ui.BaseActivity
import com.codingwithmitch.openapi.ui.Event
import com.codingwithmitch.openapi.ui.Response
import com.codingwithmitch.openapi.ui.ToolbarEvent
import com.codingwithmitch.openapi.ui.auth.AuthActivity
import com.codingwithmitch.openapi.ui.main.account.ChangePasswordFragment
import com.codingwithmitch.openapi.ui.main.account.UpdateAccountFragment
import com.codingwithmitch.openapi.ui.main.blog.UpdateBlogFragment
import com.codingwithmitch.openapi.ui.main.blog.ViewBlogFragment
import com.codingwithmitch.openapi.util.BottomNavController
import com.codingwithmitch.openapi.util.BottomNavController.*
import com.codingwithmitch.openapi.util.setUpNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemReselectedListener

class MainActivity : BaseActivity(),
    NavGraphProvider, OnNavigationGraphChanged, OnNavigationReselectedListener,
    ToolbarEvent.OnToolbarExpanded{


    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.nav_blog,
            this,
            this
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner =this
        setupActionBar()
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }
        subscribeObservers()
        subscribeListener()
    }

    private fun subscribeListener() {
        binding.apply {
        }
    }

    fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, Observer { authToken ->
            Log.d(TAG, "Mainactivity : SubscriberObservers : AuthToken : $authToken")
            if (authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                Log.d(TAG, "Mainactivity : SubscriberObservers : Logging Out")
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()


    private fun setupActionBar() {
        setSupportActionBar(binding.toolBar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }

    override fun displayProgressBar(showProgressBar: Boolean) {
        if (showProgressBar) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun getNavGraphId(itemId: Int): Int {
        Log.d(TAG,"MainActivity getNavGraphId $itemId ")
        when (itemId) {

            R.id.nav_create_blog -> {
                return R.navigation.nav_create_blog
            }
            R.id.nav_account -> {
                return R.navigation.nav_account
            }
            else -> {
                return R.navigation.nav_blog
            }

        }
    }

    override fun onGraphChanged() {
        expandAppBar()
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) =
        when (fragment) {
            is ViewBlogFragment -> {
                navController.navigate(R.id.action_viewBlogFragment_to_blogFragment)
            }
            is UpdateBlogFragment -> {
                navController.navigate(R.id.action_updateBlogFragment_to_blogFragment)
            }
            is UpdateAccountFragment -> {
                navController.navigate(R.id.action_updateAccountFragment_to_accountFragment)
            }
            is ChangePasswordFragment -> {
                navController.navigate(R.id.action_changePasswordFragment_to_accountFragment)
            }
            else -> {
                //do nothing
            }

        }

    override fun expandAppBar() {
        binding.appBar.setExpanded(true)
    }


}
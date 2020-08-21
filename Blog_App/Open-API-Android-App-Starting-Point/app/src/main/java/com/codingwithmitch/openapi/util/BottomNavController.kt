package com.codingwithmitch.openapi.util

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.util.BottomNavController.OnNavigationReselectedListener
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavController(
    val context : Context,
    @IdRes val containerId : Int,
    @IdRes val appStartDestinationId : Int,
    val grapchChangedListener : OnNavigationGraphChanged?,
    val navGraphProvider: NavGraphProvider

) {

    val TAG = "AppDebug"
    lateinit var activity : Activity
    lateinit var fragmentManager: FragmentManager
    private lateinit var navItemChangeListener : OnNavigationItemChanged
    private val navBackStack = BackStack.of(appStartDestinationId)

    init{
        if(context is Activity){
            activity = context
            fragmentManager = (activity as FragmentActivity).supportFragmentManager
        }
    }

    fun onNavigationItemSelected(itemId : Int = navBackStack.last()) : Boolean {
        val fragment = fragmentManager.findFragmentByTag(itemId.toString())
            ?: NavHostFragment.create(navGraphProvider.getNavGraphId(itemId))


        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            .replace(containerId, fragment, itemId.toString())
            .addToBackStack(null)
            .commit()

        navBackStack.moveLast(itemId)

        //Update
        navItemChangeListener.onItemChanged(itemId)

        //
        grapchChangedListener?.onGraphChanged()

        return true
    }

    fun onBackPressed(){
        val childFragmentManager = fragmentManager.findFragmentById(containerId)!!
            .childFragmentManager
        when{
            childFragmentManager.popBackStackImmediate() -> {

            }
            //Fragment bottom navigatiion backstack is empty so try to back on the navigation stack
            navBackStack.size > 1 -> {

                // Remove last item from the backstack
                navBackStack.removeLast()


                //Update the container with ne fragment
                onNavigationItemSelected()
            }

            navBackStack.last() != appStartDestinationId -> {
                navBackStack.removeLast()
                navBackStack.add(0 , appStartDestinationId)
                onNavigationItemSelected()
            }
            else -> activity.finish()
        }
    }

    private class BackStack : ArrayList<Int>(){

        companion object{
            fun of(vararg elements: Int): BackStack{
                val b = BackStack()
                b.addAll(elements.toTypedArray())
                return b
            }
        }

        fun removeLast() = removeAt(size-1)

        fun moveLast(item : Int){
            remove(item)
            add(item)
        }
    }

    //Setting the checked icon in the bottom nav
    private interface OnNavigationItemChanged {
        fun onItemChanged(itemId: Int)
    }

    fun setOnItemNavigatonChanged(listener : (itemId : Int) -> Unit){
        this.navItemChangeListener = object : OnNavigationItemChanged{
            override fun onItemChanged(itemId: Int) {
                listener.invoke(itemId)
            }

        }
    }

    /*
        Get Id of Each Grapch
        examlple :
            R.navigation.nav_blog
                or
           R.navigation.nav_create_blog
      */
    interface NavGraphProvider {
        @NavigationRes
        fun getNavGraphId(itemId : Int):Int

    }


    //Interface to be used by the activity
    interface OnNavigationGraphChanged{
        fun onGraphChanged()
    }


    // When we reselect the same bottom nav
    interface  OnNavigationReselectedListener{
        fun onReselectNavItem(navController: NavController , fragment : Fragment)
    }

}

fun BottomNavigationView.setUpNavigation(
    bottomNavController : BottomNavController,
    onNavigationReselectedListener: OnNavigationReselectedListener
) {
    setOnNavigationItemSelectedListener {
        bottomNavController.onNavigationItemSelected(it.itemId)
    }

    setOnNavigationItemReselectedListener {
        bottomNavController
            .fragmentManager
            .findFragmentById(bottomNavController.containerId)!!
            .childFragmentManager
            .fragments[0]?.let{fragment ->

            onNavigationReselectedListener.onReselectNavItem(
                bottomNavController.activity.findNavController(bottomNavController.containerId),
                fragment
            )
        }
    }

    bottomNavController.setOnItemNavigatonChanged { itemId ->
        menu.findItem(itemId).isChecked = true

    }
}
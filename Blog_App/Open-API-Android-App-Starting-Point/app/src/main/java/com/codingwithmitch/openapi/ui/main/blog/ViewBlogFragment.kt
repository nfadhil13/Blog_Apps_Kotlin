package com.codingwithmitch.openapi.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.FragmentViewBlogBinding

class ViewBlogFragment : BaseBlogFragment(){

    private var binding : FragmentViewBlogBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_blog,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // TODO("CHECK IF USER IS AUTHOR OF BLOGSPOT")
        val isAuthorOfBlogPost = true
        if(isAuthorOfBlogPost){
            inflater.inflate(R.menu.edit_view_menu , menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val isAuthorOfBlogPost = true
        if(isAuthorOfBlogPost){
            when(item.itemId){
                R.id.edit -> {
                    navUpdateBlogFragment()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun navUpdateBlogFragment(){
        findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG,"make binding null")
        binding = null
    }
}

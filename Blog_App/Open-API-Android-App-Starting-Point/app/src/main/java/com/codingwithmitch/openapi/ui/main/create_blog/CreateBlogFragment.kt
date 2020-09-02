package com.codingwithmitch.openapi.ui.main.create_blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codingwithmitch.openapi.databinding.FragmentCreateBlogBinding
import javax.inject.Inject

class CreateBlogFragment @Inject constructor(): BaseCreateBlogFragment() {

    private var _binding: FragmentCreateBlogBinding? = null

    private val binding
        get() = _binding!!

    override fun cancelActiveJobs() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateBlogBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
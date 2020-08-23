package com.codingwithmitch.openapi.ui.main.blog


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.FragmentBlogBinding
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.util.TopSpacingItemDecoration
import javax.inject.Inject

class BlogFragment : BaseBlogFragment() , BlogListAdapter.Interaction{

    private lateinit var binding : FragmentBlogBinding

    @Inject
    lateinit var requestManager: RequestManager

    lateinit var blogListAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog , container , false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObserver()
        binding.goViewBlogFragment.setOnClickListener {
            findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
        }
        executeSearch()
        initRecyclerView()
    }

    private fun executeSearch(){
        viewModel.setQuery("")
        viewModel.setStateEvent(
            BlogStateEvent.BlogSearchEvent("")
        )
    }

    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {dataState->
            dataState?.let {
                stateChangeListener.onDataStateChange(dataState)
                dataState.data?.let{data->
                    data.data?.let{event->
                        event.getContentIfNotHandled()?.let{viewState->
                            viewModel.setBlogListData(viewState.blogFields.blogList)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner , Observer { viewState->
            viewState?.let{it->
                Log.d(TAG,"BlogFragment SumbitingList")
                blogListAdapter.submitList(
                    list = it.blogFields.blogList,
                    isQueryExhausted = true
                )
            }
        })
    }

    private fun initRecyclerView(){
        binding.blogPostRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@BlogFragment.context)

            val topSpacingItemDecoration = TopSpacingItemDecoration(3)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogListAdapter(
                requestManager = requestManager,
                interaction = this@BlogFragment,
                lifeCycleOwner = viewLifecycleOwner
            )

            adapter = blogListAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if(lastPosition == blogListAdapter.itemCount.minus(1)){
                        Log.d(TAG, "BlogFragment Attempting to load next page..")

                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.blogPostRecyclerview.adapter = null
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        Log.d(TAG , "onItemSelected: position , BlogPost : $position , $item")
    }
}
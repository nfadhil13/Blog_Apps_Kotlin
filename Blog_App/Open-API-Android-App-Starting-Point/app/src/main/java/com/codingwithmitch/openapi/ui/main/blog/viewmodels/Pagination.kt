package com.codingwithmitch.openapi.ui.main.blog.viewmodels

import android.util.Log
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent.*
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
fun BlogViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.refreshFromCache(){
    if(!isJobAlreadyActive(BlogSearchEvent())){
        setQueryExhausted(false)
        setStateEvent(BlogSearchEvent(false))
    }
}
@ExperimentalCoroutinesApi
@FlowPreview
fun BlogViewModel.loadFirstPage() {
    if(!isJobAlreadyActive(BlogSearchEvent())){
        setQueryExhausted(false)
        resetPage()
        setStateEvent(BlogSearchEvent())
        Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.searchQuery}")
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun BlogViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page ?: 1
    update.blogFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.nextPage(){
    if(!isJobAlreadyActive(BlogSearchEvent())
        && !viewState.value!!.blogFields.isQueryExhausted!!
    ){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setStateEvent(BlogSearchEvent())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState){
    viewState.blogFields.let { blogFields ->
        blogFields.blogList?.let { setBlogListData(it) }
    }
}
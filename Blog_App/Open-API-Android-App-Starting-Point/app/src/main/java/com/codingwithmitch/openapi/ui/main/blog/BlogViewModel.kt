package com.codingwithmitch.openapi.ui.main.blog

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.repository.main.BlogRepository
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.BaseViewModel
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState
import com.codingwithmitch.openapi.util.AbsenLiveData
import javax.inject.Inject

class BlogViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    val blogRepository: BlogRepository,
    val sharedPreferences: SharedPreferences,
    val requestManager: RequestManager
) : BaseViewModel<BlogStateEvent, BlogViewState>() {
    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }

    override fun handleStateEvent(stateEvent: BlogStateEvent): LiveData<DataState<BlogViewState>> {
       when(stateEvent){
           is BlogStateEvent.BlogSearchEvent ->{
               return sessionManager.cachedToken.value?.let{authToken ->
                   blogRepository.searchBlogPosts(
                       authToken= authToken ,
                       query = stateEvent.query
                   )
               }?:  AbsenLiveData.create()
           }

           is BlogStateEvent.None -> {
               return AbsenLiveData.create()
           }
        }
    }

    fun setQuery(query : String){
        val update = getCurrentViewStateOrNew()
        if(update.blogFields.searchQuery == query){
            return
        }
        update.blogFields.searchQuery = query
        _viewState.value = update
    }

    fun setBlogListData(blogList : List<BlogPost>){
        val update = getCurrentViewStateOrNew()
        update.blogFields.blogList = blogList
        _viewState.value = update
    }

    fun cancelActiveJob(){
        handlePendingData()
        blogRepository.cancelAllJobs()
    }

    private fun handlePendingData(){
        setStateEvent(BlogStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJob()
    }

}
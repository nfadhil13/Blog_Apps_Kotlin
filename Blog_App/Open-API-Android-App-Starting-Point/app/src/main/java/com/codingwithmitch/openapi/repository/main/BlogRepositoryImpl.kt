package com.codingwithmitch.openapi.repository.main

import android.util.Log
import com.codingwithmitch.openapi.api.main.OpenApiMainService
import com.codingwithmitch.openapi.api.main.responses.BlogListSearchResponse
import com.codingwithmitch.openapi.dependcy_injection.main.MainScope
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.persistence.BlogPostDao
import com.codingwithmitch.openapi.persistence.returnOrderedBlogQuery
import com.codingwithmitch.openapi.repository.NetworkBoundResource
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState.*
import com.codingwithmitch.openapi.util.*
import com.codingwithmitch.openapi.util.SuccessHandler.Companion.SUCCESS_BLOG_DELETED
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@FlowPreview
@MainScope
class BlogRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): BlogRepository {

    private val TAG: String = "AppDebug"
    override fun searchBlogPosts(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<BlogViewState>> {
        return object : NetworkBoundResource<BlogListSearchResponse, List<BlogPost>, BlogViewState>(
            dispatcher = IO,
            stateEvent = stateEvent,
            apiCall = {
                openApiMainService.searchListBlogPost(
                    "Token ${authToken.token!!}",
                    query,
                    filterAndOrder,
                    page
                )
            },
            cacheCall = {
                blogPostDao.returnOrderedBlogQuery(
                    query = query,
                    filterAndOrder = filterAndOrder,
                    page = page
                )
            }
        ) {
            override suspend fun updateCache(networkObject: BlogListSearchResponse) {
                val blogPostList = networkObject.toList()
                withContext(IO) {
                    for (blogPost in blogPostList) {
                        try {
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting blog: ${blogPost}")
                                blogPostDao.insert(blogPost)
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on blog post with slug: ${blogPost.slug}. " +
                                        "${e.message}"
                            )
                            // Could send an error report here or something but I don't think you should throw an error to the UI
                            // Since there could be many blog posts being inserted/updated.
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<BlogPost>): DataState<BlogViewState> {
                val viewState = BlogViewState(
                    blogFields = BlogFields(
                        blogList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.result
    }





}

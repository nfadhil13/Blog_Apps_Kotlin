package com.codingwithmitch.openapi.ui.main.blog.state

import android.app.DownloadManager
import com.codingwithmitch.openapi.models.BlogPost

data class BlogViewState(

    var blogFields: BlogFields = BlogFields()

){


    data class BlogFields(
        var blogList : List<BlogPost> = ArrayList<BlogPost>(),
        var searchQuery: String = ""
    )

}
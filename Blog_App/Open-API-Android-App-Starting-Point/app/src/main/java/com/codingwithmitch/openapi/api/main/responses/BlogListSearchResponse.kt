package com.codingwithmitch.openapi.api.main.responses

import com.codingwithmitch.openapi.models.BlogPost
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogListSearchResponse (

    @SerializedName("results")
    @Expose
    var results : List<BlogSearchResponse>,

    @SerializedName("detail")
    @Expose
    var detail : String

){
    override fun toString(): String {
        return "BlogListSearchResponse(results = $results , detail $detail)"
    }

    fun toList() : List<BlogPost>{
        val blogPostList : ArrayList<BlogPost> = ArrayList()
        for (blogPostResponse in results){
            blogPostList.add(
                blogPostResponse.toBlogPost()
            )
        }
        return blogPostList
    }
}
package com.codingwithmitch.openapi.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED
import com.codingwithmitch.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_USERNAME
import com.codingwithmitch.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_DESC_DATE_UPDATED
import com.codingwithmitch.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_DESC_USERNAME
import com.codingwithmitch.openapi.persistence.BlogQueryUtils.Companion.TAG

class BlogQueryUtils {


    companion object{
        val TAG: String = "AppDebug"

        // values
        const val BLOG_ORDER_ASC: String = ""
        const val BLOG_ORDER_DESC: String = "-"
        const val BLOG_FILTER_USERNAME = "username"
        const val BLOG_FILTER_DATE_UPDATED = "date_updated"

        val ORDER_BY_ASC_DATE_UPDATED =  BLOG_FILTER_DATE_UPDATED + BLOG_ORDER_ASC
        val ORDER_BY_DESC_DATE_UPDATED = BLOG_FILTER_DATE_UPDATED + BLOG_ORDER_DESC
        val ORDER_BY_ASC_USERNAME =   BLOG_FILTER_USERNAME + BLOG_ORDER_ASC
        val ORDER_BY_DESC_USERNAME = BLOG_FILTER_USERNAME +  BLOG_ORDER_DESC
    }
}


suspend  fun BlogPostDao.returnOrderedBlogQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<BlogPost> {
    Log.d(TAG, "BlogDao : Query data $filterAndOrder : $ORDER_BY_ASC_USERNAME : ${filterAndOrder.equals(
        ORDER_BY_ASC_USERNAME)}")
    when{
        filterAndOrder.equals(ORDER_BY_DESC_DATE_UPDATED) ->{
            Log.d(TAG,"BlogDao : Query data DESC by DATE_UPDATED")
            return searchBlogPostsOrderByDateDESC(
                query = query,
                page = page)
        }

        filterAndOrder.equals(ORDER_BY_ASC_DATE_UPDATED) ->{
            Log.d(TAG,"BlogDao : Query data ASC by DATE_UPDATED")
            return searchBlogPostsOrderByDateASC(
                query = query,
                page = page)
        }

        filterAndOrder.equals(ORDER_BY_DESC_USERNAME) ->{
            Log.d(TAG,"BlogDao : Query data DESC by Author")
            return searchBlogPostsOrderByAuthorDESC(
                query = query,
                page = page)
        }

        else ->{
            Log.d(TAG,"BlogDao : Query data ASC by Author")
            return searchBlogPostsOrderByAuthorASC(
                query = query,
                page = page)
        }
    }
}
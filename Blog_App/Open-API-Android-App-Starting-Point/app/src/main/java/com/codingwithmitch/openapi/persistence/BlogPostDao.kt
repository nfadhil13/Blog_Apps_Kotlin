package com.codingwithmitch.openapi.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.util.Constants.Companion.PAGINATION_PAGE_SIZE

@Dao
interface BlogPostDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogPost: BlogPost) : Long

    @Query("""
        SELECT * FROM blog_post
        WHERE title LIKE '%'|| :query || '%'
        or body LIKE '%'|| :query || '%'
        or username LIKE '%'|| :query || '%'
        LIMIT (:page * :pageSize)
    """)
    suspend fun getAllBlogPost(
        query : String,
        page : Int,
        pageSize : Int = PAGINATION_PAGE_SIZE
    ) : List<BlogPost>

    @Query("""
        SELECT * FROM blog_post
        WHERE title LIKE '%'|| :query || '%'
        or body LIKE '%'|| :query || '%'
        or username LIKE '%'|| :query || '%'
        ORDER BY date_updated DESC
        LIMIT (:page * :pageSize)
    """)
    suspend fun searchBlogPostsOrderByDateDESC(
        query : String,
        page : Int,
        pageSize : Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>

    @Query("""
        SELECT * FROM blog_post
        WHERE title LIKE '%'|| :query || '%'
        or body LIKE '%'|| :query || '%'
        or username LIKE '%'|| :query || '%'
        ORDER BY date_updated ASC
        LIMIT (:page * :pageSize)
    """)
    suspend fun searchBlogPostsOrderByDateASC(
        query : String,
        page : Int,
        pageSize : Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>

    @Query("""
        SELECT * FROM blog_post
        WHERE title LIKE '%'|| :query || '%'
        or body LIKE '%'|| :query || '%'
        or username LIKE '%'|| :query || '%'
        ORDER BY username ASC
        LIMIT (:page * :pageSize)
    """)
    suspend fun searchBlogPostsOrderByAuthorASC(
        query : String,
        page : Int,
        pageSize : Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>

    @Query("""
        SELECT * FROM blog_post
        WHERE title LIKE '%'|| :query || '%'
        or body LIKE '%'|| :query || '%'
        or username LIKE '%'|| :query || '%'
        ORDER BY username DESC
        LIMIT (:page * :pageSize)
    """)
    suspend fun searchBlogPostsOrderByAuthorDESC(
        query : String,
        page : Int,
        pageSize : Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>
}
package com.codingwithmitch.openapi.dependcy_injection.main

import com.codingwithmitch.openapi.api.main.OpenApiMainService
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.persistence.AppDatabase
import com.codingwithmitch.openapi.persistence.BlogPostDao
import com.codingwithmitch.openapi.repository.main.AccountRepository
import com.codingwithmitch.openapi.repository.main.AccountRepositoryImpl
import com.codingwithmitch.openapi.repository.main.BlogRepository
import com.codingwithmitch.openapi.repository.main.BlogRepositoryImpl
import com.codingwithmitch.openapi.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit

@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder) : OpenApiMainService{
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @FlowPreview
    @JvmStatic
    @MainScope
    @Provides
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        sessionManager : SessionManager,
        accountPropertiesDao: AccountPropertiesDao
    ): AccountRepository {
        return AccountRepositoryImpl(
            openApiMainService = openApiMainService,
            sessionManager = sessionManager,
            accountPropertiesDao = accountPropertiesDao
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideBlogPostRepository(db : AppDatabase) : BlogPostDao{
        return db.getBlogPostDao()
    }

    @FlowPreview
    @JvmStatic
    @MainScope
    @Provides
    fun provideBlogRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): BlogRepository {
        return BlogRepositoryImpl(openApiMainService,blogPostDao,sessionManager)
    }

}
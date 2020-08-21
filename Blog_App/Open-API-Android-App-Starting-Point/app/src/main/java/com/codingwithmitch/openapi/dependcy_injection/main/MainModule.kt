package com.codingwithmitch.openapi.dependcy_injection.main

import com.codingwithmitch.openapi.api.main.OpenApiMainService
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.repository.main.AccountRepository
import com.codingwithmitch.openapi.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder) : OpenApiMainService{
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @MainScope
    @Provides
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        sessionManager : SessionManager,
        accountPropertiesDao: AccountPropertiesDao
    ):AccountRepository{
        return AccountRepository(
            openApiMainService = openApiMainService,
            sessionManager = sessionManager,
            accountPropertiesDao = accountPropertiesDao
        )
    }

}
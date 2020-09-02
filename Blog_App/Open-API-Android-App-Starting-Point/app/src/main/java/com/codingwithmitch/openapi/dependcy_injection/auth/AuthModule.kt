package com.codingwithmitch.openapi.dependcy_injection.auth

import android.content.SharedPreferences
import com.codingwithmitch.openapi.api.auth.OpenApiAuthService
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import com.codingwithmitch.openapi.repository.auth.AuthRepository
import com.codingwithmitch.openapi.repository.auth.AuthRepositoryImpl
import com.codingwithmitch.openapi.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit

@Module
object AuthModule {

    @JvmStatic
    @AuthScope
    @Provides
    fun provideFakeApiService(retrofitBuilder : Retrofit.Builder) : OpenApiAuthService{
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @FlowPreview
    @JvmStatic
    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService,
        sharedPreferences: SharedPreferences,
        sharedPreferencesEditor : SharedPreferences.Editor
    ) : AuthRepository {
        return AuthRepositoryImpl(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager,
            sharedPreferences,
            sharedPreferencesEditor
        )
    }

}
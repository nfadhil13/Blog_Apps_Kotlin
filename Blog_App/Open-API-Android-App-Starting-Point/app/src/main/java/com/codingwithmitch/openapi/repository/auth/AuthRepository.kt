package com.codingwithmitch.openapi.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.codingwithmitch.openapi.api.auth.OpenApiAuthService
import com.codingwithmitch.openapi.api.auth.network_responses.LoginResponse
import com.codingwithmitch.openapi.api.auth.network_responses.RegistrationResponse
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import com.codingwithmitch.openapi.repository.NetworkBoundResource
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.Response
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.auth.state.AuthViewState
import com.codingwithmitch.openapi.ui.auth.state.LogInFields
import com.codingwithmitch.openapi.ui.auth.state.RegistrationFields
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.ERROR_UNKNOWN
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.GENERIC_AUTH_ERROR
import com.codingwithmitch.openapi.util.GenericApiResponse
import com.codingwithmitch.openapi.util.GenericApiResponse.*
import kotlinx.coroutines.Job
import java.lang.Error
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
) {

    private val TAG: String = "AppDebug"

    private var repojob: Job? = null

    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
        val logInFieldsError = LogInFields(email, password).isValidLogIn()
        if (!logInFieldsError.equals(LogInFields.LoginError.none())) {
            return returnErrorResponse(logInFieldsError, ResponseType.Dialog())
        }
        return object : NetworkBoundResource<LoginResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet()
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "AuthRepository : LoginAttemp : HandleApiSuccess")

                //Incorect login credential (wHEN GET RESPONSE BUT JSON BODY RESPONSE HAS VALUE ERROR
                if (response.body.response.equals(GENERIC_AUTH_ERROR)) {
                    Log.d(TAG, "AuthRepository : LoginAttemp : Response Error")
                    return onErrorReturn(response.body.errorMessage, ResponseType.Dialog())
                }

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(
                                account_pk = response.body.pk,
                                token = response.body.token
                            )
                        ),
                        response = null
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, email)
            }

            override fun setJob(job: Job) {
                repojob?.cancel()
                repojob = job
            }

        }.asLiveData()
    }

    fun attemptRegister(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {

        //Checking field validation
        val registerFieldError = RegistrationFields(
            registration_email = email,
            registration_username = username,
            registration_password = password,
            registration_confirm_password = confirmPassword
        ).isValidForRegistration()

        //if not valid
        if (!registerFieldError.equals(RegistrationFields.RegistrationError.none())) {
            return returnErrorResponse(registerFieldError, ResponseType.Dialog())
        }

        //if valid
        return object :
            NetworkBoundResource<RegistrationResponse, AuthViewState>(sessionManager.isConnectedToTheInternet()) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {
                Log.d(TAG, "handleApiSuccessResponce : Registration Sucess Response : $response")

                if (response.body.response.equals(GENERIC_AUTH_ERROR)) {
                    return onErrorReturn(response.body.errorMessage, ResponseType.Dialog())
                }

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        ),
                        response = null
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, username, password, confirmPassword)
            }

            override fun setJob(job: Job) {
                repojob?.cancel()
                repojob = job
            }

        }.asLiveData()

    }

    fun cancleActiveJobs() {
        Log.d("TAG", "AuthRepository : Cancelling on-going jobs...")
        repojob?.cancel()
    }

    private fun returnErrorResponse(errorMessage: String, dialog: ResponseType.Dialog)
            : LiveData<DataState<AuthViewState>> {
        return liveData {
            emit(
                DataState.error(
                    Response(
                        errorMessage,
                        dialog
                    )
                )
            )
        }
    }

//    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
//        return openApiAuthService.login(email, password)
//            .switchMap { response ->
//                object : LiveData<DataState<AuthViewState>>() {
//                    override fun onActive() {
//                        super.onActive()
//
//                        when (response) {
//                            is ApiSuccessResponse -> {
//                                value = DataState.data(
//                                    AuthViewState(
//                                        authToken = AuthToken
//                                            (
//                                            account_pk = response.body.pk,
//                                            token = response.body.token
//                                        )
//                                    ),
//                                    response = null
//                                )
//                            }
//
//                            is ApiErrorResponse -> {
//                                value = DataState.error(
//                                    response = Response(
//                                        message = response.errorMessage,
//                                        responseType = ResponseType.Dialog()
//                                    )
//                                )
//                            }
//
//                            is ApiEmptyResponse -> {
//                                value = DataState.error(
//                                    response = Response(
//                                        message = ERROR_UNKNOWN,
//                                        responseType = ResponseType.Dialog()
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//    }

//    fun attemptRegister(
//        email: String,
//        username : String,
//        password: String,
//        confirmPassword : String
//    ): LiveData<DataState<AuthViewState>> {
//        return openApiAuthService.register(email, username, password , confirmPassword)
//            .switchMap { response ->
//                object : LiveData<DataState<AuthViewState>>() {
//                    override fun onActive() {
//                        super.onActive()
//
//                        when (response) {
//                            is ApiSuccessResponse -> {
//                                value = DataState.data(
//                                    AuthViewState(
//                                        authToken = AuthToken
//                                            (
//                                            account_pk = response.body.pk,
//                                            token = response.body.token
//                                        )
//                                    ),
//                                    response = null
//                                )
//                            }
//
//                            is ApiErrorResponse -> {
//                                value = DataState.error(
//                                    response = Response(
//                                        message = response.errorMessage,
//                                        responseType = ResponseType.Dialog()
//                                    )
//                                )
//                            }
//
//                            is ApiEmptyResponse -> {
//                                value = DataState.error(
//                                    response = Response(
//                                        message = ERROR_UNKNOWN,
//                                        responseType = ResponseType.Dialog()
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//    }
}
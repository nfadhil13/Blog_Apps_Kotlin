package com.codingwithmitch.openapi.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.codingwithmitch.openapi.api.auth.OpenApiAuthService
import com.codingwithmitch.openapi.api.auth.network_responses.LoginResponse
import com.codingwithmitch.openapi.api.auth.network_responses.RegistrationResponse
import com.codingwithmitch.openapi.models.AccountProperties
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import com.codingwithmitch.openapi.repository.JobManager
import com.codingwithmitch.openapi.repository.NetworkBoundResource
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.Data
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.Response
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.auth.state.AuthViewState
import com.codingwithmitch.openapi.ui.auth.state.LogInFields
import com.codingwithmitch.openapi.ui.auth.state.RegistrationFields
import com.codingwithmitch.openapi.util.*
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.ERROR_SAVE_AUTH_TOKEN
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.ERROR_UNKNOWN
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.GENERIC_AUTH_ERROR
import com.codingwithmitch.openapi.util.GenericApiResponse.*
import com.codingwithmitch.openapi.util.SuccessHandler.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.Job
import java.lang.Error
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharefPreferencesEditor: SharedPreferences.Editor
) : JobManager("AuthRepository") {

    private val TAG: String = "AppDebug"


    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
        val logInFieldsError = LogInFields(email, password).isValidLogIn()
        if (!logInFieldsError.equals(LogInFields.LoginError.none())) {
            return returnErrorResponse(logInFieldsError, ResponseType.Dialog())
        }
        return object : NetworkBoundResource<LoginResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "AuthRepository : LoginAttemp : HandleApiSuccess")

                //Incorect login credential (wHEN GET RESPONSE BUT JSON BODY RESPONSE HAS VALUE ERROR
                if (response.body.response.equals(GENERIC_AUTH_ERROR)) {
                    Log.d(TAG, "AuthRepository : LoginAttemp : Response Error")
                    return onErrorReturn(response.body.errorMessage, ResponseType.Dialog())
                }

                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        pk = response.body.pk,
                        email = response.body.email,
                        username = ""
                    )
                )

                val result = authTokenDao.insert(
                    AuthToken(
                        account_pk = response.body.pk,
                        token = response.body.token
                    )
                )

                if (result < 0) {
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                saveAuthenticatedUserToPrefs(email)

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
                return openApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
                addJob("attemptLogin",job)
            }

            override suspend fun createCacheRequestAndReturn() {
                // Not used in this case
            }

            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsenLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: Any?) {
                // Not used in this case
            }

        }.asLiveData()
    }

    private fun saveAuthenticatedUserToPrefs(email: String) {
        sharefPreferencesEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
        sharefPreferencesEditor.apply()
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
            NetworkBoundResource<RegistrationResponse, Any, AuthViewState>
                (sessionManager.isConnectedToTheInternet(),
                true,
                true,
                false) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {
                Log.d(TAG, "handleApiSuccessResponce : Registration Sucess Response : $response")

                if (response.body.response.equals(GENERIC_AUTH_ERROR)) {
                    return onErrorReturn(response.body.errorMessage, ResponseType.Dialog())
                }


                //Insert into account properties
                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        pk = response.body.pk,
                        email = response.body.email,
                        username = ""
                    )
                )

                val result = authTokenDao.insert(
                    AuthToken(
                        account_pk = response.body.pk,
                        token = response.body.token
                    )
                )

                if (result < 0) {
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                saveAuthenticatedUserToPrefs(email)


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
             addJob("attemptRegister",job)
            }


            //Not used in this case
            override suspend fun createCacheRequestAndReturn() {
            }

            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsenLiveData.create()
            }


            //Not used in this case
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

        }.asLiveData()

    }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>> {
        val previousEmail: String? =
            sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)

        Log.d(TAG, "AuthRepository : checkPreviousAuthUser")

        if (previousEmail.isNullOrBlank()) {
            Log.d(
                TAG,
                "AuthRepository : checkPreviousAuthUser : No previously authenticated user found"
            )
            return returnNoTokeFound()
        }

        return object : NetworkBoundResource<Void, Any, AuthViewState>
            (sessionManager.isConnectedToTheInternet(),
            false,
            false,
            false) {

            override suspend fun createCacheRequestAndReturn() {
                Log.d(TAG,"AuthRepository : Checking Auth Token for email $previousEmail")
                accountPropertiesDao.searchByEmail(previousEmail).let { accountProperties ->
                    accountProperties?.let {
                        Log.d(TAG, "AuthRepository : CheckPreviousAuthUser : AccountProperties Not Null")
                        if (accountProperties.pk > -1) {
                            Log.d(TAG,"AuthRepository : CheckPreviousAuthUser : PK > -1")
                            authTokenDao.searchByPk(accountProperties.pk).let { authToken ->
                                if (authToken != null) {
                                    Log.d(TAG, "AuthRepository :CheckPrevious AuthUser : AuthToken found $authToken")
                                    onCompleteJob(
                                        DataState.data(
                                            data = AuthViewState(
                                                authToken = authToken
                                            ),
                                            response = null
                                        )
                                    )
                                    return
                                }

                            }
                        }
                        Log.d(TAG, "AuthRepository :CheckPrevious AuthUser : pk < -1")
                        onCompleteJob(
                            DataState.data(
                                null,
                                Response(
                                    RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                    ResponseType.None()
                                )
                            )
                        )

                    } ?:
                    Log.d(TAG, "AuthRepository :CheckPrevious AuthUser : pk < -1")
                    onCompleteJob(
                        DataState.data(
                            null,
                            Response(
                                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                ResponseType.None()
                            )
                        )
                    )
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {
                //TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsenLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("checkPreviousAuthUser",job)
            }

            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsenLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: Any?) {
                //Not used in this case
            }

        }.asLiveData()
    }

    private fun returnNoTokeFound(): LiveData<DataState<AuthViewState>> {
        Log.d(TAG , "AuthRepository No TokenFound")
        return liveData<DataState<AuthViewState>>  {
            emit(
                DataState.data(null, Response(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None()))
            )
        }
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


}
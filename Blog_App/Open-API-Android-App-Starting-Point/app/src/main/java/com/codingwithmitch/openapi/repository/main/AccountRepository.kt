package com.codingwithmitch.openapi.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.room.Update
import com.codingwithmitch.openapi.api.GenericResponse
import com.codingwithmitch.openapi.api.main.OpenApiMainService
import com.codingwithmitch.openapi.models.AccountProperties
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.repository.JobManager
import com.codingwithmitch.openapi.repository.NetworkBoundResource
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.Response
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.auth.state.AuthViewState
import com.codingwithmitch.openapi.ui.main.account.state.AccountViewState
import com.codingwithmitch.openapi.ui.main.account.state.ChangePasswordFields
import com.codingwithmitch.openapi.util.AbsenLiveData
import com.codingwithmitch.openapi.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager
) : JobManager("AccountRepository"){
    private val TAG = "AppDebug"



    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ) {
            override suspend fun createCacheRequestAndReturn() {
                withContext(Main) {

                    //finish by viewing the db cache
                    result.addSource(loadFromCache()) { viewState ->
                        onCompleteJob(
                            DataState.data(
                                data = viewState,
                                response = null
                            )
                        )
                    }

                }
            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<AccountProperties>) {
                updateLocalDb(response.body)
                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<AccountProperties>> {
                Log.d(TAG, "CreateCall ThreadName : ${Thread.currentThread().name}")
                return openApiMainService
                    .getAccountProperties(
                        "Token ${authToken.token}"
                    )
            }

            override fun setJob(job: Job) {
                addJob("getAccountProperties", job)
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
                return accountPropertiesDao.searchByPk(authToken.account_pk!!)
                    .switchMap {
                        liveData {
                            emit(AccountViewState(it))
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: AccountProperties?) {
                cacheObject?.let {
                    accountPropertiesDao.updateAccountProperties(
                        pk = it.pk,
                        email = it.email,
                        username = it.username
                    )
                }
            }

        }.asLiveData()
    }

    fun saveAccountProperties(authToken: AuthToken, accountProperties: AccountProperties)
    :LiveData<DataState<AccountViewState>>{
        val testAccountProp = AccountViewState(accountProperties)
        if(!testAccountProp.isValidForRegistration().equals(AccountViewState.UpdateError.none())){
            return returnErrorResponse(testAccountProp.isValidForRegistration(), ResponseType.Dialog())
        }

        return object : NetworkBoundResource<GenericResponse , Any , AccountViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {

                updateLocalDb(null)
                withContext(Main){
                    //finish with success response
                    onCompleteJob(
                        DataState.data(
                            data = null ,
                            response = Response(response.body.respone, ResponseType.Toast())
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.saveAccountProperties(
                    "Token ${authToken.token!!}",
                    accountProperties.email,
                    accountProperties.username
                )
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
                return AbsenLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: Any?) {
                accountPropertiesDao.updateAccountProperties(
                    accountProperties.pk,
                    accountProperties.email,
                    accountProperties.username
                )
            }

            override fun setJob(job: Job) {
                addJob("saveAccountProperties",job)
            }

        }.asLiveData()
    }


    fun changePassword(
        authToken: AuthToken,
        currentPassword : String,
        newPassword : String,
        confirmNewPassword  : String
    )
            :LiveData<DataState<AccountViewState>>{
        Log.d(TAG,"AccountRepository : ChangePassword ")
        val changePasswordFieldsError = ChangePasswordFields(currentPassword,newPassword,confirmNewPassword)
            .isValidForChangePassword()


        if(!changePasswordFieldsError
                .equals(ChangePasswordFields.ChangePasswordError.valid())){
            Log.d(TAG,"AccountRepository : ChangePassword Input Error $changePasswordFieldsError")
            return returnErrorResponse(changePasswordFieldsError,ResponseType.Dialog())
        }
        return object : NetworkBoundResource<GenericResponse,Any, AccountViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){
            //Not used in this case
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {
                withContext(Main){
                    Log.d(TAG,"AccountRepository : ChangePassword : Successs : ${response.body.respone} ")
                    onCompleteJob(
                        DataState.data(
                            data=null,
                            response = Response(response.body.respone , ResponseType.Toast())
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                Log.d(TAG,"AccountRepository : ChangePassword : Create change password API call ")
                return openApiMainService.updatePassword(
                    "Token ${authToken.token!!}",
                    currentPassword,
                    newPassword,
                    confirmNewPassword
                )
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
            //Not used in this case
                return AbsenLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: Any?) {
            //Not used in this case
            }

            override fun setJob(job: Job) {
                addJob("changePassword",job)
            }

        }.asLiveData()
    }
    private fun returnErrorResponse(errorMessage : String, dialog: ResponseType.Dialog): LiveData<DataState<AccountViewState>> {
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
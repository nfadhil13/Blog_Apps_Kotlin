package com.codingwithmitch.openapi.repository

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.Response
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.auth.state.AuthViewState
import com.codingwithmitch.openapi.util.Constants.Companion.NETWORK_TIMEOUT
import com.codingwithmitch.openapi.util.Constants.Companion.TESTING_NETWORK_DELAY
import com.codingwithmitch.openapi.util.ErrorHandler
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.ERROR_UNKNOWN
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.UNABLE_TODO_OPERATION_WITHOUT_INTERNET
import com.codingwithmitch.openapi.util.ErrorHandler.Companion.UNABLE_TO_RESOLVE_HOST
import com.codingwithmitch.openapi.util.GenericApiResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Error

abstract class NetworkBoundResource<ResponseObject, ViewStateType>(
    isNetworkAvailable: Boolean //availabilty of network connection
) {

    private val TAG: String = "AppDebug"

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init{
        setJob(initNewJob())
        setValue(DataState.loading(isLoading = true , cachedData = null))

        if(isNetworkAvailable){
            coroutineScope.launch {

                //Testing delay
                delay(TESTING_NETWORK_DELAY)
                withContext(Main){
                    //Make network call
                    val apiResponse = createCall()
                    result.addSource(apiResponse){response ->
                        result.removeSource(apiResponse)
                        coroutineScope.launch {
                            handleNetworkCall(response)
                        }
                    }
                }
            }

            GlobalScope.launch(IO){
                delay(NETWORK_TIMEOUT)
                if(!job.isCompleted){
                    Log.e(TAG , "NetworkBoundResource : JOB NETWORK TIMEOUT")
                    job.cancel(CancellationException(UNABLE_TO_RESOLVE_HOST))
                }
            }
        }else{
            onErrorReturn(UNABLE_TODO_OPERATION_WITHOUT_INTERNET, ResponseType.Dialog())

        }
    }

    suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
        when (response) {
            is GenericApiResponse.ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }

            is GenericApiResponse.ApiErrorResponse -> {
                Log.e(TAG , "NetworkBoundResource : handleNetworkCall : ${response.errorMessage}")
                onErrorReturn(response.errorMessage , ResponseType.Dialog())
            }

            is GenericApiResponse.ApiEmptyResponse -> {
                Log.e(TAG , "NetworkBoundResource : handleNetworkCall :" +
                        " Requested Network returned Nothing (HTTP 204)")
                onErrorReturn("HTTP 204. Returned Nothing" , ResponseType.Dialog())
            }
        }
    }

    fun onCompleteJob(dataState: DataState<ViewStateType>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
    }

    private fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }

    fun onErrorReturn(errorMessage: String?, responseType: ResponseType) {
        var msg = errorMessage
        var _responseType: ResponseType = responseType
        if (msg == null) {
            msg = ERROR_UNKNOWN
        } else if (ErrorHandler.isNetworkError(msg)) {
            msg = ERROR_CHECK_NETWORK_CONNECTION
            if(responseType is ResponseType.Dialog) _responseType = ResponseType.Toast()
        }

        onCompleteJob(DataState.error(
            response = Response(
                message = msg,
                responseType = _responseType
            )
        ))

    }

    private fun initNewJob(): Job {
        Log.d(TAG, "initNewJob : called..")
        job = Job()
        job.invokeOnCompletion { cause ->
            if (job.isCancelled) {
                Log.e(TAG, "NetworkBoundResource : Job has been cancelled")
                cause?.let {
                    onErrorReturn(it.message, ResponseType.Toast())
                } ?: onErrorReturn(ERROR_UNKNOWN, ResponseType.Toast())
            } else if (job.isCompleted) {
                Log.e(TAG, "NetworkBoundResource : Job has been completed")
            }
        }

        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

    abstract suspend fun  handleApiSuccessResponse(response : GenericApiResponse.ApiSuccessResponse<ResponseObject>)

    abstract fun createCall() : LiveData<GenericApiResponse<ResponseObject>>

    abstract fun setJob(job : Job)

}
package com.codingwithmitch.openapi.util

import android.util.Log
import retrofit2.Response

sealed class GenericApiResponse<T> {

    companion object{
        private val TAG  = "AppDebug"

        fun <T> create(error:Throwable) : ApiErrorResponse<T>{
            return ApiErrorResponse(errorMessage = error.message ?: "Unknown Error")
        }

        fun <T> create(response : Response<T>) : GenericApiResponse<T>{
            Log.d(TAG , "GenericApiResponse: response: $response")
            Log.d(TAG,"GenericApiResponse: response: ${response.raw()}")
            Log.d(TAG,"GenericApiResponse: response: ${response.headers()}")
            Log.d(TAG,"GenericApiResponse: response: ${response.message()}")

            if(response.isSuccessful){
                val body = response.body()
                if(body==null || response.code()==204)
                    return ApiEmptyResponse()
                if(response.code() == 401)
                    return ApiErrorResponse("401 Unauthorize. Token may be invalid.")
                //Else
                return ApiSuccessResponse(body)
            }else{
                val msg = response.errorBody()?.string()
                val errorMsg =
                    if(msg.isNullOrEmpty()) response.message() else msg
                return ApiErrorResponse(errorMsg?: "Unknown Error")
            }

        }
    }

    class ApiEmptyResponse<T> : GenericApiResponse<T>()
    data class ApiSuccessResponse<T>(val body: T) : GenericApiResponse<T>() {}
    class ApiErrorResponse<T>(val errorMessage : String) : GenericApiResponse<T>()

}

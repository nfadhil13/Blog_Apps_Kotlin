package com.codingwithmitch.openapi.util

class ErrorHandler {
    companion object {
        const val  UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WITHOUT_INTERNET =
            "Can't do that operation withou internet connection"
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection"

        const val GENERIC_AUTH_ERROR = "Error"
        const val ERROR_SAVE_AUTH_TOKEN = "Error saving authentication token"
        const val ERROR_SAVE_ACCOUNT_PROPERTIES = "Error saving account properties"


        const val ERROR_UNKNOWN = "Unknown Error."

        fun isNetworkError(msg : String) : Boolean{
            if(msg.contains(UNABLE_TO_RESOLVE_HOST)) return true
            return false
        }
    }
}
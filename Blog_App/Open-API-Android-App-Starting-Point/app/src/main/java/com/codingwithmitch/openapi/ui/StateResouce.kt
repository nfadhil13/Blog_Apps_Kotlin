package com.codingwithmitch.openapi.ui

data class Loading(val isLoading:Boolean)
data class Data<T>(val data : Event<T>? , val response : Event<Response>?)
data class StateError(val response : Response)

data class Response(val message : String? , val responseType : ResponseType)

sealed class ResponseType{

    class Toast : ResponseType()

    class Dialog : ResponseType()

    class None : ResponseType()

}

open class Event<out T>(private val content : T){

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled() : T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    fun peekContent() : T = content

    override fun toString(): String {
        return "Event(content=$content, hasBeenHandle=$hasBeenHandled"
    }

    companion object{
        private val TAG = "AppDebug"


        // Action to prevent the data from null
        fun<T> dataEvent(data:T?) : Event<T>?{
            data?.let {
                return Event(it)
            }
            return null
        }

        //prevent respone from null
        fun responseEvent(response: Response?) : Event<Response>?{
            response?.let{
                return Event(response)
            }
            return null
        }
    }



}


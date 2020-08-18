package com.codingwithmitch.openapi.util

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<R>(private val responeType : Type)
    : CallAdapter<R,LiveData<GenericApiResponse<R>>> {
    override fun adapt(call: Call<R>): LiveData<GenericApiResponse<R>> {
        return object : LiveData<GenericApiResponse<R>>(){
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if(started.compareAndSet(false,true)){
                    call.enqueue(object : Callback<R>{
                        override fun onFailure(call: Call<R>, t: Throwable) {
                            postValue(GenericApiResponse.create(t))
                        }

                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(GenericApiResponse.Companion.create(response))
                        }
                    })
                }
            }
        }
    }

    override fun responseType() = responeType


}
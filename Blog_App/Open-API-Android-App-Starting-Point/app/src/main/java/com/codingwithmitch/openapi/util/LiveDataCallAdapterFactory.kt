package com.codingwithmitch.openapi.util

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory  : CallAdapter.Factory(){
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if(getRawType(returnType) != LiveData::class.java) return null

        //Else
        val observableType = getParameterUpperBound(0,returnType as ParameterizedType)
        val rawObservable = getRawType(observableType)
        if(rawObservable != GenericApiResponse::class.java)
            throw IllegalArgumentException("type must be a resource")

        if(observableType !is ParameterizedType)
            throw IllegalArgumentException("resource must be parameterized")

        val bodyType = getParameterUpperBound(0,observableType)
        return LiveDataCallAdapter<Any>(bodyType)
    }

}
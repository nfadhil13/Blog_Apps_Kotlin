package com.codingwithmitch.openapi.util

import androidx.lifecycle.LiveData

class AbsenLiveData < T : Any?> private constructor() : LiveData<T>(){
    init{
        postValue(null)
    }

    companion object{
        fun <T> create() : LiveData<T>{
            return AbsenLiveData()
        }
    }
}
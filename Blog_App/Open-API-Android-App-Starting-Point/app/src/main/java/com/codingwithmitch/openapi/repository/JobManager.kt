package com.codingwithmitch.openapi.repository

import android.util.Log
import kotlinx.coroutines.Job

open class JobManager (
    private val className : String

){
    private val TAG = "AppDebug"

    private val jobs : HashMap<String, Job> = HashMap()

    fun addJob(methodName : String , job : Job){
        cancelJob(methodName)
        jobs[methodName] = job
    }

    fun cancelJob(methodName: String){
        getJob(methodName)?.cancel()
    }

    private fun getJob(methodName: String): Job? {
        if(jobs.containsKey(methodName))
            return jobs[methodName] else return null
    }

    fun cancelAllJobs(){
        for((methodName,job) in jobs){
            if(job.isActive){
                Log.d(TAG ,"JobManager : Cancelling job in method: $methodName.")
                job.cancel()
            }
        }
    }
}
package com.codingwithmitch.openapi.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
)
{
    private val TAG : String = "AppDebug"

    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken : LiveData<AuthToken>
        get() = _cachedToken

    fun login(newValue : AuthToken){
        Log.d(TAG , "SessionManager : Login")
        setValue(newValue)
    }

    fun logout(){
        Log.d(TAG, "SessionManager : Logout ..")

        GlobalScope.launch(IO) {
            var errorMesaage : String? = null
            try{
                _cachedToken.value!!.account_pk?.let { authTokenDao.nullifyToken(it)
                } ?: throw CancellationException("Token Error. Logging out user.")
            }catch (e : CancellationException){
                Log.e(TAG , "logout cancellation : ${e.message}")
                errorMesaage = e.message
            } catch (e : Exception){
                Log.e(TAG , "logout exception : ${e.message}")
                errorMesaage = errorMesaage + "\n" + e.message
            }
            finally {
                errorMesaage?.let {
                    Log.e(TAG , "final exception : ${errorMesaage}")
                }
                Log.d(TAG , "logout : finally..")
                setValue(null)
            }
        }
    }

    fun setValue(newValue: AuthToken?){
        //Global scope to make sure it set the value from the main thread
        GlobalScope.launch(Main) {
            if(_cachedToken.value !=newValue){
                Log.d(TAG , "SessionManager : SetValue from ${_cachedToken.value} to $newValue")
                _cachedToken.value = newValue
            }
        }
    }

    fun isConnectedToTheInternet() : Boolean{
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try{
            return cm.activeNetworkInfo.isConnected
        }catch (e : Exception){
            Log.e(TAG , "isConnectedToInternet : ${e.message} ")
        }
        return false
    }
}
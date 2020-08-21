package com.codingwithmitch.openapi.ui.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.FragmentForgotPasswordBinding
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.DataStateChangeListener
import com.codingwithmitch.openapi.ui.Response
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.auth.ForgotPasswordFragment.WebAppInterface.OnWebInteractionCallBack
import com.codingwithmitch.openapi.util.Constants
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_launcher.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ClassCastException

class ForgotPasswordFragment : BaseAuthFragment() {

    private var  binding : FragmentForgotPasswordBinding? = null

    lateinit var stateChangeListener : DataStateChangeListener


    val webInteractionCallback: OnWebInteractionCallBack = object : OnWebInteractionCallBack{
        override fun onSuccess(email: String) {
            Log.d(TAG,"ForgotPasswordFragment : Reset Link will be sent to $email")
            onPasswordResetLinkSent()
        }

        override fun onError(errorMessage: String) {
           Log.d(TAG,"ForgotPasswordFragment : On Error : $errorMessage")

            val dataState = DataState.error<Any>(
                response = Response(errorMessage,ResponseType.Dialog())
            )

            stateChangeListener.onDataStateChange(
                dataState = dataState
            )
        }

        override fun onLoading(isLoading: Boolean) {
            Log.d(TAG , "ForgotPasswordFragment : OnLoading")
            GlobalScope.launch(Main){
                DataState.loading(isLoading = isLoading,cachedData = null)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_forgot_password, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPasswordResetWebView()
        binding!!.returnToLauncherFragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadPasswordResetWebView(){
        stateChangeListener.onDataStateChange(
            DataState.loading(isLoading = true , cachedData = null)
        )

        binding!!.webview.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                stateChangeListener.onDataStateChange(
                    DataState.loading(isLoading = false , cachedData = null)
                )
            }
        }

        binding!!.webview.loadUrl(Constants.PASSWORD_RESET_URL)
        binding!!.webview.settings.javaScriptEnabled=true
        binding!!.webview.addJavascriptInterface(WebAppInterface(webInteractionCallback),"AndroidTextListener")

    }
    private fun onPasswordResetLinkSent() {
        GlobalScope.launch(Main) {
            binding!!.apply {
                parentView.removeView(webview)
                webview.destroy()

                val animation = TranslateAnimation(
                    passwordResetDoneContainer.width.toFloat(),
                    0f,
                    0f,
                    0f
                )
                passwordResetDoneContainer.startAnimation(animation)
                passwordResetDoneContainer.visibility = View.VISIBLE
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch (e : ClassCastException){
            Log.e(TAG,"$context must implemet DataStateChangeListener")
        }
    }
    class WebAppInterface
    constructor(
        private val callback : OnWebInteractionCallBack
    ){
        private val TAG = "AppDebug"

        @JavascriptInterface
        fun onSuccess(email:String){
            callback.onSuccess(email)
        }

        @JavascriptInterface
        fun onError(message:String){
            callback.onError(message)
        }
        @JavascriptInterface
        fun onLoading(isLoading: Boolean){
            callback.onLoading(isLoading)
        }

        interface OnWebInteractionCallBack {
            fun onSuccess(email : String)
            fun onError(errorMessage : String)
            fun onLoading(isLoading : Boolean)
        }

    }







}



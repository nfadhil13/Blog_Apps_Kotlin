package com.codingwithmitch.openapi.ui

import com.codingwithmitch.openapi.util.Response
import com.codingwithmitch.openapi.util.StateMessageCallback

interface UICommunicationListener{

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean)

    fun hideSoftKeyboard()

    fun expandAppBar()
}
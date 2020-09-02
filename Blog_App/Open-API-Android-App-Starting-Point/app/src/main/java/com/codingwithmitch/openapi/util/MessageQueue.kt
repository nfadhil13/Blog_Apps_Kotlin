package com.codingwithmitch.openapi.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.IndexOutOfBoundsException

const val MESSAGE_STACK_BUNDLE_KEY = "com.codingwithmitch.openapi.util.MessageStack"

class MessageQueue : ArrayList<StateMessage>(){

    private val TAG = "AppDebug"

    private val _stateMessage : MutableLiveData<StateMessage?> = MutableLiveData()

    val stateMessage : LiveData<StateMessage?>
        get() = _stateMessage

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        for(element in elements){
            add(element)
        }
        return true
    }

    override fun add(element: StateMessage): Boolean {
        if(this.contains(element))
            return false
        val transaction = super.add(element)
        if(this.size == 1){
            setStateMessage(stateMessage = element)
        }
        return transaction
    }

    override fun removeAt(index: Int): StateMessage {
        try{
            val transaction = super.removeAt(index)
            if(this.size >0){
                setStateMessage(stateMessage = this[0])
            }else{
                Log.d(TAG,"Stack is empty")
                setStateMessage(null)
            }
            return transaction
        }catch(e : IndexOutOfBoundsException){
            e.printStackTrace()
        }
        return StateMessage(
            Response(
                message = "does nothing",
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.None()
            )
        )
    }

    private fun setStateMessage(stateMessage: StateMessage?) {
        _stateMessage.value = stateMessage
    }
}
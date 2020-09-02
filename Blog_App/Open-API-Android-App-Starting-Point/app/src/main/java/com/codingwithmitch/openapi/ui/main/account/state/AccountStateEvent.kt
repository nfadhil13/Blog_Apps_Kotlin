package com.codingwithmitch.openapi.ui.main.account.state

import com.codingwithmitch.openapi.util.StateEvent

sealed class AccountStateEvent : StateEvent{

    class GetAccountPropertiesEvent : AccountStateEvent() {
        override fun errorInfo(): String {
            return "Error getting Account info"
        }

        override fun toString(): String {
            return "GetAccountPropertiesEvent"
        }
    }

    data class ChangePasswordEvent(
        val oldPassword : String,
        val newPassword : String,
        val newPasswordConfirm : String
    ) : AccountStateEvent() {
        override fun errorInfo(): String {
            return "Error changing the password"
        }

        override fun toString(): String {
            return "ChangePasswordEvent"
        }
    }

    data class UpdateAccountPropertiesEvent(
        val email : String,
        val username : String
    ) : AccountStateEvent() {
        override fun errorInfo(): String {
            return "Error Updating last Account"
        }

        override fun toString(): String {
            return "UpdateAccountPropertiesEvent"
        }
    }

    class None : AccountStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

        override fun toString(): String {
            return "None"
        }
    }
}
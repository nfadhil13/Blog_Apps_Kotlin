package com.codingwithmitch.openapi.ui.main.account.state

sealed class AccountStateEvent {

    class GetAccountPropertiesEvent : AccountStateEvent()

    data class ChangePasswordEvent(
        val oldPassword : String,
        val newPassword : String,
        val newPasswordConfirm : String
    ) : AccountStateEvent()

    data class UpdateAccountPropertiesEvent(
        val email : String,
        val username : String
    ) : AccountStateEvent()

    class None : AccountStateEvent()
}
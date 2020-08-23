package com.codingwithmitch.openapi.ui.main.account.state

import android.util.Log
import com.codingwithmitch.openapi.models.AccountProperties
import com.codingwithmitch.openapi.ui.auth.state.RegistrationFields

data class AccountViewState(
    var accountProperties: AccountProperties? = null,
    var changePasswordFields: ChangePasswordFields? = ChangePasswordFields()

){
    class UpdateError{
        companion object{
            fun mustFillAllFields() : String{
                return "All fields are required"
            }

            fun invalidEmail() : String{
                return "Invalid Email"
            }

            fun invalidUsername() : String{
                return "Username not allowed to be blank"
            }

            fun none() : String{
                return "none"
            }
            fun propertiesNull() : String{
                return "Properties Null"
            }
        }
    }

    fun isValidForRegistration() : String{
        accountProperties?.let{
            if(it.email.isNullOrBlank() || !it.email.contains("@"))
                return UpdateError.invalidEmail()
            if(it.username.isNullOrBlank()){
                return UpdateError.invalidUsername()
            }
            if(it.email.isNullOrBlank() && it.username.isNullOrBlank())
                return UpdateError.mustFillAllFields()
            return UpdateError.none()
        }
        return UpdateError.propertiesNull()

    }
}

data class ChangePasswordFields(
    var oldPassword : String? = null ,
    var newPassword : String? = null ,
    var newPasswordConfirm : String? =null
){
    class ChangePasswordError{
        companion object{
            fun mustFillAllFields() : String{
                return "All fields are required"
            }

            fun confirmNewPasswordNotSame() : String{
                return "Confirm Password Not Same"
            }

            fun valid() : String{
                return "valid"
            }

        }
    }

    fun isValidForChangePassword() : String{
        Log.d("AppDebug","$this")
        if(newPassword.isNullOrBlank() && oldPassword.isNullOrBlank() && newPasswordConfirm.isNullOrBlank())
            return ChangePasswordError.mustFillAllFields()
        if(!newPassword.equals(newPasswordConfirm))
            return ChangePasswordError.confirmNewPasswordNotSame()
        return ChangePasswordError.valid()


    }

    override fun toString(): String {
        return "oldPassword = $oldPassword , newPassword = $newPassword , confirmPassword = $newPasswordConfirm"
    }

}



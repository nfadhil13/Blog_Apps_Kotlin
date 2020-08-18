package com.codingwithmitch.openapi.ui.auth.state

import com.codingwithmitch.openapi.models.AuthToken

data class AuthViewState(
    var registrationFields : RegistrationFields? = RegistrationFields(),
    var logInField: LogInFields? = LogInFields(),
    var authToken: AuthToken? = null
)

data class RegistrationFields(
    var registration_email : String? = null,
    var registration_username : String? = null,
    var registration_password : String? = null,
    var registration_confirm_password : String? = null
){
    class RegistrationError{
        companion object{
            fun mustFillAllFields() : String{
                return "All fields are required"
            }

            fun passwordDoNotMatch() : String{
                return "Password must match"
            }

            fun none() : String{
                return "none"
            }
        }
    }

    fun isValidForRegistration() : String{
        if(registration_email.isNullOrEmpty()
            || registration_username.isNullOrEmpty()
            || registration_password.isNullOrEmpty()
            || registration_confirm_password.isNullOrEmpty()){
            return RegistrationError.mustFillAllFields()
        }

        if(!registration_password.equals(registration_confirm_password))
            return RegistrationError.passwordDoNotMatch()

        return RegistrationError.none()

    }

}

data class LogInFields(
    var login_email : String? = null,
    var login_password : String? = null
){
    class LoginError{
        companion object{
            fun mustFillAllFields() : String{
                return "All fields are required"
            }

            fun none() : String{
                return "none"
            }
        }
    }

    fun isValidLogIn() : String{
        if(login_email.isNullOrEmpty() || login_password.isNullOrEmpty())
            return LoginError.mustFillAllFields()
        return LoginError.none()
    }
}
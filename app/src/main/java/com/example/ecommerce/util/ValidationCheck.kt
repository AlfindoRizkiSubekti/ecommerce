package com.example.ecommerce.util

import android.util.Patterns


fun validationEmail(email: String): RegisterValidation {
    if(email.isEmpty())
        return RegisterValidation.Failed("Email cannot be empty")

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong Email format")

    return RegisterValidation.Success
}

fun validationPassword(password: String): RegisterValidation {
    if(password.isEmpty())
        return RegisterValidation.Failed("Password cannot be empty")

    if(password.length <= 6)
        return RegisterValidation.Failed("Password should contains 6 word")

    return RegisterValidation.Success
}
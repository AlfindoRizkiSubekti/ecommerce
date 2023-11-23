package com.example.ecommerce.viewModels

import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.User
import com.example.ecommerce.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisteViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val resgister: Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWhitEmailAndPassword(user: User, password: String){
        if (checkValidation(user, password)){
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener{
                    it.user?.let{
                        _register.value = Resource.Success(it)
                    }
                }.addOnFailureListener{
                    _register.value = Resource.Error(it.message.toString())
                }
        }else {
            val registerFieldsState = RegisterFieldsState(
                validationEmail(user.email), validationPassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun checkValidation(user: User, password: String): Boolean{
        val emailValidation = validationEmail(user.email)
        val passwordValidation = validationPassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Success
                && passwordValidation is RegisterValidation.Success
        return shouldRegister
    }
}



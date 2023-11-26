package com.example.ecommerce.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.util.Constants.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private  val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate = _navigate

    companion object{
        const val SHOPPING_ACTIVITY = 23
        const val ACCOUNT_OPTION_FRAGMENT = R.id.action_introductionFragment_to_accountOptionsFragment
    }

    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if(user != null){
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        }else if(isButtonClicked){
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTION_FRAGMENT)
            }
        }else{
            Unit
        }
    }

    fun startButtonClick(){
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true)
    }
}
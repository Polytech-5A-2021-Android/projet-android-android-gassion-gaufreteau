package com.polytech.projet_android_iot.viewmodelfactory.exemples

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.dao.UserDao
import com.polytech.projet_android_iot.model.User
import com.polytech.projet_android_iot.viewmodel.exemples.PersonalDataViewModel

class PersonalDataViewModelFactory(
    private val user: User,
    private val dataSource: UserDao,
    private val application: Application,
    private val userID: Long = 0L // userID
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalDataViewModel::class.java)) {
            return PersonalDataViewModel(user,dataSource,application,userID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
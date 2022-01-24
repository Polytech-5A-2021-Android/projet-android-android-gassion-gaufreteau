package com.polytech.projet_android_iot.viewmodelfactory.exemples

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.dao.UserDao
import com.polytech.projet_android_iot.viewmodel.exemples.IdentityViewModel

class IdentityViewModelFactory (
    private val dataSource: UserDao,
    private val application: Application,
    private val userID: Long = 0L // userID

) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IdentityViewModel::class.java)) {
            return IdentityViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
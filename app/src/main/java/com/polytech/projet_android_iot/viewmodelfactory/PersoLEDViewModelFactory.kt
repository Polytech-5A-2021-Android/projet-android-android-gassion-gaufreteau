package com.polytech.projet_android_iot.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.viewmodel.PersoLEDViewModel

class PersoLEDViewModelFactory(
    private val dataSource: UserIOTDao,
    private val application: Application,
    private val uid: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersoLEDViewModel::class.java)) {
            return PersoLEDViewModel(dataSource,application,uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
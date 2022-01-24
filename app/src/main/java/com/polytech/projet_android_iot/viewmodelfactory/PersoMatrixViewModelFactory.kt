package com.polytech.projet_android_iot.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.viewmodel.PersoMatrixViewModel

class PersoMatrixViewModelFactory(
    private val dataSource: UserIOTDao,
    private val application: Application,
    private val uid: Long,
    private val bid: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersoMatrixViewModel::class.java)) {
            return PersoMatrixViewModel(dataSource,application,uid,bid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
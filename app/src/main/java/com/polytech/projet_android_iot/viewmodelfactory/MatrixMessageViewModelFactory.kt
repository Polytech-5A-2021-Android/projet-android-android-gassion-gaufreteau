package com.polytech.projet_android_iot.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.viewmodel.MatrixMessageViewModel

class MatrixMessageViewModelFactory(
    private val dataSource: UserIOTDao,
    private val application: Application,
    private val uid: Long,
    private val bid: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatrixMessageViewModel::class.java)) {
            return MatrixMessageViewModel(dataSource,application,uid,bid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
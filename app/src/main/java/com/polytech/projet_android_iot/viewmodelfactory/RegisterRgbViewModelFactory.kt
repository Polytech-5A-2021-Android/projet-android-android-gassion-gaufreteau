package com.polytech.projet_android_iot.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.viewmodel.RegisterRgbViewModel

class RegisterRgbViewModelFactory(
    private val dataSource: UserIOTDao,
    private val application: Application,
    private val uid: Long,
    private val bid: Long,
    private val led1: String,
    private val led2: String,
    private val led3: String,
    ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterRgbViewModel::class.java)) {
            return RegisterRgbViewModel(dataSource,application,uid,bid,led1,led2,led3) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
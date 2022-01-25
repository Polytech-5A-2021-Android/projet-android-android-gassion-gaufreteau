package com.polytech.projet_android_iot.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.dao.BoardIOTDao
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.viewmodel.BoardConnectViewModel

class BoardConnectViewModelFactory(
    private val dataSource: UserIOTDao,
    private val dataSourceBoard: BoardIOTDao,
    private val application: Application,
    private val uid: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardConnectViewModel::class.java)) {
            return BoardConnectViewModel(dataSource,dataSourceBoard,application,uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
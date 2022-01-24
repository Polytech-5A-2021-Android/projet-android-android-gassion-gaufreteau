package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*

class PersoLEDViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long // UID
) : AndroidViewModel(application){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    init {
        Log.i("PersoLEDViewModel", "created")
        initializeUser()
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = getUser()
        }
    }

    private suspend fun getUser(): UserIOT? {
        return withContext(Dispatchers.IO) {
            var user = database.get(userID)
            user
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("PersoLEDViewModel", "destroyed")
        viewModelJob.cancel()
    }
}

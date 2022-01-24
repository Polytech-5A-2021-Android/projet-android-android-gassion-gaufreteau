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

class RGBPickerViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long // UID
) : AndroidViewModel(application){

    private var colorPicked1 = ""
    private var colorPicked2 = ""
    private var colorPicked3 = ""
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user

    init {
        Log.i("RGBPickerViewModel", "created")
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

    fun setColor(led: Int, color: String) {
        when (led) {
            1 -> colorPicked1 = color
            2 -> colorPicked2 = color
            3 -> colorPicked3 = color
        }
    }

    fun getColor(led: Int): String {
        when (led) {
            1 -> return colorPicked1
            2 -> return colorPicked2
            3 -> return colorPicked3
        }
        return ""
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("RGBPickerViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
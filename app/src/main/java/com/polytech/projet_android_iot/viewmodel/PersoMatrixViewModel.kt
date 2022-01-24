package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*
import java.lang.Exception

class PersoMatrixViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long // UID
) : AndroidViewModel(application){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )


    init {
        Log.i("PersoMatrixViewModel", "created")
        initializeUser()
    }

    fun switchSoundDetFromAPI(b: Boolean): Boolean {
        var res = false
        coroutineScope.launch {
            var switchSoundDetectorDeferred = MyApiIOT.retrofitService.switchSoundDetector(b)
            try {
                var resSwitch = switchSoundDetectorDeferred.await()
                res = resSwitch
            }catch (e: Exception) {

            }
        }
        return res
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = getUser()
        }
    }

    private suspend fun getUser(): UserIOT? {
        return withContext(Dispatchers.IO) {
            val user = database.get(userID)
            user
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("PersoMatrixViewModel", "destroyed")
        viewModelJob.cancel()
    }

}
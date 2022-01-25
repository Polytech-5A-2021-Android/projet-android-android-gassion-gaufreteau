package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.UserIOT
import com.polytech.projet_android_iot.switchSD
import kotlinx.coroutines.*
import java.lang.Exception

class PersoMatrixViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long, // UID
    private var boardID: Long // BID
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

    fun switchSoundDetFromAPI(b: Boolean) {
        val switchSD = switchSD(boardID,b)
        coroutineScope.launch {
            val switchSoundDetectorDeferred = MyApiIOT.retrofitService.switchSoundDetector(switchSD)
            try {
                val resSwitch = switchSoundDetectorDeferred.await()
                _res.value = resSwitch.confirm
            }catch (e: Exception) {
                _res.value = false
            }
        }
    }

    private val _res = MutableLiveData<Boolean?>()

    val res: MutableLiveData<Boolean?>
        get() = _res

    fun doneNavigating() {
        _res.value = null
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
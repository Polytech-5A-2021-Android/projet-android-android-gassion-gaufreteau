package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.DispayMess
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*
import java.lang.Exception

class MatrixMessageViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long, // UID
    private val boardID : Long // BID
) : AndroidViewModel(application){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )


    init {
        Log.i("MatrixMessageViewModel", "created")
        initializeUser()
    }

    private fun displayMessageFromAPI(message: String) {
        val messageObj = DispayMess(boardID,message)
        coroutineScope.launch {
            val displayMessageDeferred = MyApiIOT.retrofitService.displayMessage(messageObj)
            try {
                val msgResult = displayMessageDeferred.await()
                _navigateToHomeFragment.value = msgResult.confirm
            }catch (e: Exception) {
                Log.i("API -- Message", "Exception with API")
                _navigateToHomeFragment.value = false
            }
        }
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
        Log.i("MatrixMessageViewModel", "destroyed")
        viewModelJob.cancel()
    }
    /**
     * Error manager when registering
     * If it is null, no error
     * Each value corresponds to an error
     */
    private val _errorRegistering = MutableLiveData<Long?>()

    val errorRegistering: MutableLiveData<Long?>
        get() = _errorRegistering

    private fun noError() {
        _errorRegistering.value = null
    }

    private val _navigateToHomeFragment = MutableLiveData<Boolean?>()

    val navigateToHomeFragment: MutableLiveData<Boolean?>
        get() = _navigateToHomeFragment

    fun doneNavigating() {
        _navigateToHomeFragment.value = null
    }

    fun onValidateMessage() {
        uiScope.launch {
            val message = message.value
            if(message.isNullOrEmpty()) {
                _errorRegistering.value = 1
                return@launch
            }
            displayMessageFromAPI(message)
            noError()
        }
    }
}
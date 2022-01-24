package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.ChangePwd
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*
import java.lang.Exception

class ParamsViewModel(
    val database: UserIOTDao,
    application: Application,
    private val userID: Long // UID
) : AndroidViewModel(application){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val _pwdCheck = MutableLiveData<String>()
    val _newPwd = MutableLiveData<String>()
    val _oldPwd = MutableLiveData<String>()
    val user: LiveData<UserIOT>
        get() = _user
    val newPwd: LiveData<String>
        get() = _newPwd
    val pwdCheck: LiveData<String>
        get() = _pwdCheck
    val oldPwd: LiveData<String>
        get() = _oldPwd

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        Log.i("ParamsViewModel", "created")
        initializeUser()
    }

    private fun changePwdFromAPI(oldPwd: String, newPwd: String){
        coroutineScope.launch {
            val changeObj = ChangePwd(userID,newPwd,oldPwd)
            var changePwdDeferred = MyApiIOT.retrofitService.changePwd(changeObj)
            try {
                var objResult = changePwdDeferred.await()
                if(objResult.id== _user.value!!.id) {
                    _user.value = objResult
                    noError()
                    _navigateToHomeFragment.value = _user.value!!.id
                }else _errorRegistering.value = 0
            }catch (e: Exception) {
                Log.i("API ERROR -- ChangePwd", "Exception with API -- Using local DB")
                val user = _user.value
                user!!.password =newPwd
                updatePwd(user)
                noError()
                _navigateToHomeFragment.value = user.id
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
        Log.i("ParamsViewModel", "destroyed")
        viewModelJob.cancel()
    }

    private val _navigateToHomeFragment = MutableLiveData<Long?>()

    val navigateToHomeFragment: MutableLiveData<Long?>
        get() = _navigateToHomeFragment

    fun doneNavigating() {
        _navigateToHomeFragment.value = null
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

    fun onValidateUpdate() {
        uiScope.launch {

            val user = user.value ?: return@launch
            val pwdCheck = pwdCheck.value ?: return@launch
            val newPwd = newPwd.value ?: return@launch
            val oldPwd = oldPwd.value ?: return@launch

            if(oldPwd.isEmpty())
                return@launch

            if(pwdCheck.isEmpty())
                return@launch

            if(newPwd.isEmpty())
                return@launch

            if(oldPwd!=user.password) {
                _errorRegistering.value = 2
                return@launch
            }
            if(pwdCheck != newPwd){
                _errorRegistering.value = 1
                return@launch
            }
            changePwdFromAPI(newPwd,oldPwd)
        }
    }

    private suspend fun updatePwd(user: UserIOT) {
        withContext(Dispatchers.IO) {
            database.update(user)
        }
    }
}
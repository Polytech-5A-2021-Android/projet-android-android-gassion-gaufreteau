package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.LoginInfo
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*
import java.lang.Exception

class LoginViewModel(
    val database: UserIOTDao,
    application: Application,
    private val userID: Long= 0L // UID
) : AndroidViewModel(application)
{
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    /**
     * Login with API -- If no API, login with app DB
     */
    private fun loginFromAPI(user: UserIOT) {
        coroutineScope.launch {
            val loginfo = LoginInfo(user.login,user.password)
            val loginDeferred = MyApiIOT.retrofitService.login(loginfo)
            try {
                val loginResult = loginDeferred.await()
                if(loginResult.response) {
                    noError()
                    _navigateToHomeFragment.value = loginResult.id
                }else{
                    _errorLogin.value = true
                    return@launch
                }
            }catch (e: Exception) {
                Log.i("API ERROR -- Login", "Exception with API -- Using local DB")
                val uid = getUserLoginStatus(user.login,user.password)
                if(uid!=null) {
                    _navigateToHomeFragment.value = uid
                    noError()
                }else{
                    _errorLogin.value = true
                    return@launch
                }
            }
        }
    }


    init {
        Log.i("LoginViewModel", "created")
        initializeUser()
        uiScope.launch {
            insertBaseUser()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("LoginViewModel", "destroyed")
        viewModelJob.cancel()
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = UserIOT()
        }
    }

    /**
     * Inserting a basic user in DB so we can login with it
     * The goal is to make to application usable without API
     */
    private suspend fun insertBaseUser() {
        val usertmp = UserIOT(1,"Axel","Axel")
        withContext(Dispatchers.IO) {
            if(database.get(1)==null) {
                database.insert(usertmp)
                Log.i("User inserted", "Success")
            }
        }
    }

    private suspend fun getUserLoginStatus(login: String?, pwd: String?): Long? {
        user.value
        return withContext(Dispatchers.IO) {
            val userSel = database.getByLoginAndPwd(login, pwd)
            userSel?.id
        }
    }

    private val _navigateToHomeFragment = MutableLiveData<Long?>()

    val navigateToHomeFragment: MutableLiveData<Long?>
        get() = _navigateToHomeFragment

    fun doneNavigating() {
        _navigateToHomeFragment.value = null
    }

    private val _errorLogin = MutableLiveData<Boolean?>()

    val errorLogin: MutableLiveData<Boolean?>
        get() = _errorLogin

    private fun noError() {
        _errorLogin.value = null
    }

    fun onValidateIdentity() {
        uiScope.launch {

            val user = user.value ?: return@launch

            if(user.login.isNullOrEmpty())
                return@launch

            if(user.password.isNullOrEmpty())
                return@launch

            loginFromAPI(user)
        }
    }
}
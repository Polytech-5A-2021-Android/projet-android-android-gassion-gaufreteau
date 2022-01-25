package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.LongConverter
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*
import java.lang.Exception

class RegisterViewModel(
    val database: UserIOTDao,
    application: Application,
) : AndroidViewModel(application)
{
    val countries = arrayListOf<String>("France", "Djibouti", "Ouzbékistan")
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val _pwdCheck = MutableLiveData<String>()
    val user: LiveData<UserIOT>
        get() = _user
    val pwdCheck: LiveData<String>
        get() = _pwdCheck

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        Log.i("RegisterViewModel", "created")
        initializeUser()
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = UserIOT()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("RegisterViewModel", "destroyed")
        viewModelJob.cancel()
    }

    fun getCountryPos(pos: Int) {
        _user.value?.country = countries[pos]
    }

    fun onGender(gender: String) {
        _user.value?.gender = gender
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

    fun onValidateRegister() {
        uiScope.launch {

            val user = user.value ?: return@launch
            val pwdCheck = pwdCheck.value

            if(user.login.isNullOrEmpty()) {
                _errorRegistering.value = 8
                return@launch
            }

            if(user.password.isNullOrEmpty()) {
                _errorRegistering.value = 7
                return@launch
            }

            if(pwdCheck.isNullOrEmpty()) {
                _errorRegistering.value = 9
                return@launch
            }

            if(user.address.isNullOrEmpty()) {
                _errorRegistering.value = 6
                return@launch
            }

            if(user.town.isNullOrEmpty()) {
                _errorRegistering.value = 5
                return@launch
            }

            if(user.firstname.isNullOrEmpty()) {
                _errorRegistering.value = 4
                return@launch
            }

            if(user.lastname.isNullOrEmpty()) {
                _errorRegistering.value = 3
                return@launch
            }

            if(user.gender.isNullOrEmpty()) {
                _errorRegistering.value = 2
                return@launch
            }

            if(user.country.isNullOrEmpty()) {
                _errorRegistering.value = 10
                return@launch
            }

            if(pwdCheck != user.password) {
                _errorRegistering.value = 1
                return@launch
            }

            if(LongConverter.dateToString(user.birthdayDate)=="01/01/70") {
                _errorRegistering.value = 11
                return@launch
            }

            if(!loginExists(user.login!!)) {
                _errorRegistering.value = 12
                return@launch
            }

            registerFromAPI(user)

        }
    }

    fun setBday(bday: String) {
        _user.value!!.birthdayDate = LongConverter.stringToDate(bday)
    }

    private suspend fun insert(user: UserIOT): Long {
        var id: Long
        withContext(Dispatchers.IO) {
            id = database.insert(user)
        }
        return id
    }

    private suspend fun loginExists(login: String): Boolean {
        return withContext(Dispatchers.IO) {
            val res = database.getByLogin(login)==null
            res
        }
    }

    /**
     * Register with API -- If no API, register into app DB
     */
    private fun registerFromAPI(user: UserIOT) {
        coroutineScope.launch {
            val registerDeferred = MyApiIOT.retrofitService.register(user)
            try {
                val registerResult = registerDeferred.await()
                _user.value = registerResult
                _navigateToHomeFragment.value = _user.value!!.id
            }catch (e: Exception) {
                Log.i("API ERROR -- Login", "Exception with API -- Using local DB")
                val uid = insert(user)
                noError()
                _navigateToHomeFragment.value = uid
            }
        }
    }
}
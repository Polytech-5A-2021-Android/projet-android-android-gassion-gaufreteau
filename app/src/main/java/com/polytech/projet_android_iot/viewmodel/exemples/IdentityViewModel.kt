package com.polytech.projet_android_iot.viewmodel.exemples

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.dao.UserDao
import com.polytech.projet_android_iot.model.User
import kotlinx.coroutines.*

class IdentityViewModel(
    val database: UserDao,
    application: Application,
    private val userID: Long = 0L // userID
) : AndroidViewModel(application){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        Log.i("IdentityViewModel", "created")
        initializeUser()
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = getUserFromDatabase()
        }
    }

    private suspend fun getUserFromDatabase(): User? {
        return withContext(Dispatchers.IO) {

            var user = database.get(userID) // userID
            if (user == null) {
                user = User()
                user.id = insert(user)
            }
            user
        }
    }

    private suspend fun insert(user: User): Long {
        var id = 0L
        withContext(Dispatchers.IO) {
            id = database.insert(user)
        }
        return id
    }

    fun onValidate() {
        uiScope.launch {
            val user = user.value ?: return@launch
            update(user)
        }
    }

    private suspend fun update(user: User) {
        withContext(Dispatchers.IO) {
            database.update(user)
        }
    }

    private suspend fun get(id: Long) {
        withContext(Dispatchers.IO) {
            database.get(id)
        }
    }




    override fun onCleared() {
        super.onCleared()
        Log.i("IdentityViewModel", "destroyed")
        viewModelJob.cancel()
    }

    fun onGender(gender: String) {
        _user.value?.gender = gender
    }

    private val _navigateToPersonalDataFragment = MutableLiveData<User?>()

    val navigateToPersonalDataFragment: MutableLiveData<User?>
        get() = _navigateToPersonalDataFragment

    fun doneNavigating() {
        _navigateToPersonalDataFragment.value = null
    }

    fun onValidateIdentity() {
        uiScope.launch {
            val user = user.value ?: return@launch

            if(user.firstname.isNullOrEmpty())
                return@launch

            if(user.lastname.isNullOrEmpty())
                return@launch

            update(user)

            _navigateToPersonalDataFragment.value = user
        }
    }
}
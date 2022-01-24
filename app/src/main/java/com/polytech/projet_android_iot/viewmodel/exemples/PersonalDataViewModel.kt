package com.polytech.projet_android_iot.viewmodel.exemples

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.dao.UserDao
import com.polytech.projet_android_iot.model.User
import kotlinx.coroutines.*

class PersonalDataViewModel(
    userParam: User,
    val database: UserDao,
    application: Application,
    private val userID: Long = 0L // userID
) : AndroidViewModel(application)
{
    private val _user = MutableLiveData<User>()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val user: LiveData<User>
        get() = _user

    init {
        Log.i("PersonalDataViewModel", "created")
        _user.value=userParam
    }

    fun onGender(gender: String) {
        _user?.value?.gender = gender
    }

    private val _navigateToOtherActivity = MutableLiveData<User>()

    val navigateToOtherActivity: LiveData<User>
        get() = _navigateToOtherActivity

    fun doneValidateNavigating() {
        _navigateToOtherActivity.value = null
    }

    fun onValidate() {
        uiScope.launch {
            val user = user.value ?: return@launch

            if(user.gender.isNullOrEmpty())
                return@launch

            update(user)

            _navigateToOtherActivity.value = user
        }
    }

    private suspend fun update(user: User) {
        withContext(Dispatchers.IO) {
            database.update(user)
        }
    }

}
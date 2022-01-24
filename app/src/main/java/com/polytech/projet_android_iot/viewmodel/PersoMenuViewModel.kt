package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.db.DatabasePresets
import com.polytech.projet_android_iot.model.BoardIOT
import com.polytech.projet_android_iot.model.PresetsIOT
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*

class PersoMenuViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long // UID
) : AndroidViewModel(application){

    private val databasePresets = DatabasePresets.getInstance(application).presetsIOTDao
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user



    init {
        Log.i("PersoMenuViewModel", "created")
        initializeUser()
        uiScope.launch{
            insertBaseBoards()
        }
    }

    /**
     * Inserting a basic preset in DB
     * The goal is to make to application usable without API
     */
    private suspend fun insertBaseBoards() {
        val pretmp = PresetsIOT(1,1L,"Preset 1.1")
        val pretmp2 = PresetsIOT(2,2L, "Preset 1.2")
        withContext(Dispatchers.IO) {
            if(databasePresets.get(1)==null) {
                databasePresets.insert(pretmp)
                Log.i("Preset inserted", "Success")
            }
            if(databasePresets.get(2)==null) {
                databasePresets.insert(pretmp2)
                Log.i("Preset inserted", "Success")
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
            var user = database.get(userID)
            user
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("PersoMenuViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
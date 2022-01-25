package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.db.DatabasePresets
import com.polytech.projet_android_iot.model.PresetsIOT
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*
import java.lang.Exception

class PresetLEDViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long, // UID
    private var boardID: Long // BID
) : AndroidViewModel(application){

    private val databasePresets = DatabasePresets.getInstance(application).presetsIOTDao
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    private val _presets = MutableLiveData<List<PresetsIOT>>()
    val presets: LiveData<List<PresetsIOT>>
        get() = _presets
    private val _resUse = MutableLiveData<Boolean?>()
    val resUse: MutableLiveData<Boolean?>
        get() = _resUse

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )


    init {
        Log.i("PresetLEDViewModel", "created")
        initializeUser()
        initializePresets()
    }

    private fun initializePresets() {
        getPresetsFromAPI()
    }

    /**
     * Here : API call for db stored boards
     */
    private suspend fun getPresetsFromDatabase(): List<PresetsIOT>? {
        return withContext(Dispatchers.IO) {
            val presets = databasePresets.getPresets(boardID)
            presets
        }
    }

    private fun getPresetsFromAPI() {
        coroutineScope.launch {
            val getPresetsDeferred = MyApiIOT.retrofitService.getPresets(boardID)
            try {
                val listResult = getPresetsDeferred.await()
                _presets.value = listResult
            }catch (e: Exception) {
                Log.i("API ERROR -- Presets", "Exception with API -- Using local DB")
                _presets.value = getPresetsFromDatabase()
            }
        }
    }

    fun usePresetFromAPI(pid: Long) {
        coroutineScope.launch {
            val usePresetDeferred = MyApiIOT.retrofitService.usePreset(pid)
            try {
                val boolResult = usePresetDeferred.await()
                Log.i("API -- Use of Presets", "Result of call : ${boolResult.confirm}")
                _resUse.value = boolResult.confirm
            } catch (e: Exception) {
                Log.i("API -- Use Presets", "Exception with API")
                _resUse.value = false
            }
        }
    }


    fun doneNavigating() {
        _resUse.value = null
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
        Log.i("PresetLEDViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
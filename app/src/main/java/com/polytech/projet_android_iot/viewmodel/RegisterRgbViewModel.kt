package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.db.DatabaseIotBoard
import com.polytech.projet_android_iot.db.DatabasePresets
import com.polytech.projet_android_iot.model.PresetsIOT
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*
import java.lang.Exception

class RegisterRgbViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long, // UID
    private var boardID: Long, // BID
    private val led1: Int,
    private val led2: Int,
    private val led3: Int,
) : AndroidViewModel(application){

    private val databasePresets = DatabasePresets.getInstance(application).presetsIOTDao
    private val databaseBoards = DatabaseIotBoard.getInstance(application).boardIOTDao
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    private val _presets = MutableLiveData<List<PresetsIOT>>()
    val presets: LiveData<List<PresetsIOT>>
        get() = _presets
    val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )


    init {
        Log.i("RegisterRgbViewModel", "created")
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
            var getPresetsDeferred = MyApiIOT.retrofitService.getPresets(boardID)
            try {
                var listResult = getPresetsDeferred.await()
                _presets.value = listResult
            }catch (e: Exception) {
                Log.i("API ERROR -- Presets", "Exception with API (CreationList) -- Using local DB")
                _presets.value = getPresetsFromDatabase()
            }
        }
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = getUser()
        }
    }


    private fun createPresetFromAPI(preset: PresetsIOT) {
        coroutineScope.launch {
            var createPresetDeferred = MyApiIOT.retrofitService.createPreset(preset)
            try {
                var presetResult = createPresetDeferred.await()
                Log.i("API -- NewPreset", "Result of preset creation, pid : " + presetResult.id)
            }catch (e: Exception) {
                Log.i("API ERROR -- NewPreset", "Exception with API -- Using local DB")
                insertPreset(preset)
                noError()
                _navigateToHomeFragment.value = preset.id
            }
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
        Log.i("RegisterRgbViewModel", "destroyed")
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

    fun onValidateName(name: String) {
        uiScope.launch {

            if(!checkValidity(name)) {
                _errorRegistering.value = 1
                return@launch
            }

            val preset = setPreset(name)
            createPresetFromAPI(preset)
        }
    }

    private fun setPreset(name: String): PresetsIOT {
        val preset = PresetsIOT()
        preset.bid = boardID
        preset.name = name
        preset.led1 = led1
        preset.led2 = led2
        preset.led3 = led3
        return preset
    }

    private suspend fun checkValidity(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            databasePresets.getByName(name,boardID)==null
        }
    }

    private suspend fun insertPreset(preset: PresetsIOT) {
        withContext(Dispatchers.IO) {
            databasePresets.insert(preset)
        }
    }
}
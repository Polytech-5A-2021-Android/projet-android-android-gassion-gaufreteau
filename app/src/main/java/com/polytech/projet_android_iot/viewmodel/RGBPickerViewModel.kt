package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.PresetsIOT
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*
import java.lang.Exception

class RGBPickerViewModel(
    val database: UserIOTDao,
    application: Application,
    private var userID: Long, // UID
    private var boardID: Long // BID
) : AndroidViewModel(application){

    private var colorPicked1 = ""
    private var colorPicked2 = ""
    private var colorPicked3 = ""
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )


    init {
        Log.i("RGBPickerViewModel", "created")
        initializeUser()
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

    private fun initFakePreset(): PresetsIOT {
        var preset = PresetsIOT()
        preset.bid = boardID
        preset.led1 = colorPicked1
        preset.led2 = colorPicked2
        preset.led3 = colorPicked3
        return preset
    }

    private val _colors = MutableLiveData<Boolean?>()
    val colors: MutableLiveData<Boolean?>
        get() = _colors
    fun doneNavigating() {
        _colors.value = null
    }

    fun onValidateColors() {
        val fakePreset = initFakePreset()
        coroutineScope.launch {
            val useColorsDeferred = MyApiIOT.retrofitService.useColors(fakePreset)
            try {
                val boolResult = useColorsDeferred.await()
                Log.i("API -- Use Colors", "Result of call : ${boolResult.confirm}")
                _colors.value=boolResult.confirm
            }catch (e: Exception) {
                Log.i("API -- Use Colors", "Exception with API")
                _colors.value = false
            }
        }
    }

    fun setColor(led: Int, color: String) {
        when (led) {
            1 -> colorPicked1 = color
            2 -> colorPicked2 = color
            3 -> colorPicked3 = color
        }
    }

    fun getColor(led: Int): String {
        when (led) {
            1 -> return colorPicked1
            2 -> return colorPicked2
            3 -> return colorPicked3
        }
        return ""
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("RGBPickerViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
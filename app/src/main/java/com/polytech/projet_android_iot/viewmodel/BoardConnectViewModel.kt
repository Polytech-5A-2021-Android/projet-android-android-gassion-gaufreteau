package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.MyApiIOT
import com.polytech.projet_android_iot.dao.BoardIOTDao
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.BoardIOT
import com.polytech.projet_android_iot.model.UserIOT
import com.polytech.projet_android_iot.verifCode
import kotlinx.coroutines.*
import java.lang.Exception

class BoardConnectViewModel(
    val database: UserIOTDao,
    private val databaseBoard: BoardIOTDao,
    application: Application,
    private var userID: Long, // UID
) : AndroidViewModel(application){


    private var board: BoardIOT
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    val _boardCode = MutableLiveData<String>()
    val boardCode: LiveData<String>
        get() = _boardCode
    var _boardSyncMessage = MutableLiveData<String>()
    val boardSyncMessage: LiveData<String>
        get() = _boardSyncMessage
    val _boardConfirm = MutableLiveData<String>()
    val boardConfirm: LiveData<String>
        get() = _boardConfirm
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )


    init {
        Log.i("BoardConnectViewModel", "created")
        initializeUser()
        board = BoardIOT()
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
        Log.i("BoardConnectViewModel", "destroyed")
        viewModelJob.cancel()
    }

    fun launchSync()  {
        uiScope.launch {
            val code = boardCode.value ?: return@launch
            boardDetection(code)
        }
    }

    private val _boardDetected = MutableLiveData<Boolean?>()

    val boardDetected: MutableLiveData<Boolean?>
        get() = _boardDetected

    fun boardDetectionDone() {
        _boardDetected.value = null
    }

    private fun boardDetection(code: String) {
        coroutineScope.launch {
            val syncBoardDeferred = MyApiIOT.retrofitService.syncBoard(code.toLong())
            try {
                val objResult = syncBoardDeferred.await()
                _boardDetected.value = objResult.confirm
            }catch (e: Exception) {
                Log.i("API ERROR -- Sync", "Exception with API -- Using local DB")
                noError()
                _boardDetected.value = (code=="123")
            }
        }
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

    fun onValidateSync(bc: String) {
        uiScope.launch {
            if(bc.isEmpty())
                return@launch
            validateSyncAPI(bc.toLong())
        }
    }


    private val _boardSynced = MutableLiveData<Boolean?>()

    val boardSynced: MutableLiveData<Boolean?>
        get() = _boardSynced

    fun boardSyncDone() {
        _boardSynced.value = null
    }

    private fun validateSyncAPI(code: Long?) {
        coroutineScope.launch {
            val verifcode = verifCode(userID, _boardCode.value?.toLong(), code)
            val verifyCodeDeferred = MyApiIOT.retrofitService.verifyCode(verifcode)
            try {
                val objResult = verifyCodeDeferred.await()
                _boardSynced.value = objResult.confirm
            }catch (e: Exception) {
                Log.i("API ERROR -- Sync", "Exception with API -- Using local DB")
                noError()
                if(code!=123L) {
                    _errorRegistering.value = 1
                    return@launch
                }else{
                    board.name="BoardTEST"
                    insert(board)
                    noError()
                }
                _boardSynced.value = (code==123L)
            }
        }
    }

    private suspend fun insert(board: BoardIOT) {
        withContext(Dispatchers.IO) {
            databaseBoard.insert(board)
        }
    }

}
package com.polytech.projet_android_iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.projet_android_iot.dao.BoardIOTDao
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.BoardIOT
import com.polytech.projet_android_iot.model.UserIOT
import kotlinx.coroutines.*

class BoardConnectViewModel(
    val database: UserIOTDao,
    val databaseBoard: BoardIOTDao,
    application: Application,
    private var userID: Long, // UID
) : AndroidViewModel(application){


    private var board: BoardIOT
    private var boardDetected = false
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    val _boardCode = MutableLiveData<String>()
    val boardCode: LiveData<String>
        get() = _boardCode
    private var _boardSyncMessage = MutableLiveData<String>()
    val boardSyncMessage: LiveData<String>
        get() = _boardSyncMessage
    val _boardConfirm = MutableLiveData<String>()
    val boardConfirm: LiveData<String>
        get() = _boardConfirm


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
            if(boardDetected) {
                _boardSyncMessage.value = "Board detected"
                board.name = code
            }else{
                _boardSyncMessage.value = "Board not detected"
            }
        }
    }

    private suspend fun boardDetection(code: String) {
        return withContext(Dispatchers.IO) {
            //API CALL TO BOARD
            /*
            if(code is correct) ? boardDetected=true : boardDetected=false
             */
            boardDetected = code=="123"
            }
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

    fun onValidateSync(bc: String) {
        uiScope.launch {

            if(bc.isEmpty())
                return@launch

            //API CALL TO CHECK CONFIRM VALIDITY
            if(bc != "123"){
                _errorRegistering.value = 1
                return@launch
            }
            if(boardCode.value!=board.name){
                _errorRegistering.value = 2
                return@launch
            }
/*            if(!getBoard(board.name)) {
                _errorRegistering.value = 3
                return@launch
            }*/
            insert(board)
            noError()
            _navigateToHomeFragment.value = userID
        }

    }

    private suspend fun insert(board: BoardIOT) {
        withContext(Dispatchers.IO) {
            databaseBoard.insert(board)
        }
    }

    private suspend fun getBoard(name: String?): Boolean {
        return withContext(Dispatchers.IO) {
            databaseBoard.getBoardByName(name)==null
        }
    }

}
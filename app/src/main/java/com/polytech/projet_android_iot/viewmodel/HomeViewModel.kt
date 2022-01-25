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
import kotlinx.coroutines.*
import java.lang.Exception

class HomeViewModel(
    val database: UserIOTDao,
    private val databaseBoard: BoardIOTDao,
    application: Application,
    private var userID: Long // UID
) : AndroidViewModel(application){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _user = MutableLiveData<UserIOT>()
    val user: LiveData<UserIOT>
        get() = _user
    private val _boards = MutableLiveData<List<BoardIOT>>()
    val boards: LiveData<List<BoardIOT>>
        get() = _boards

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        Log.i("HomeViewModel", "created")
        initializeUser()
        uiScope.launch {
            insertBaseBoards()
        }
        initializeBoards()
    }
    /**
     * Inserting a basic board in DB
     * The goal is to make to application usable without API
     */
    private suspend fun insertBaseBoards() {
        val boardtmp = BoardIOT(1,"FakeBoard")
        val boardtmp2 = BoardIOT(2,"FakeBoard 2")
        withContext(Dispatchers.IO) {
            if(databaseBoard.get(1)==null) {
                databaseBoard.insert(boardtmp)
                Log.i("Board inserted", "Success")
            }
            if(databaseBoard.get(2)==null) {
                databaseBoard.insert(boardtmp2)
                Log.i("Board inserted", "Success")
            }
        }
    }

    private fun initializeBoards() {
        getBoardsFromAPI()
    }

    /**
     * Here : API call for db stored boards
     */
    private suspend fun getBoardsFromDatabase(): List<BoardIOT> {
        return withContext(Dispatchers.IO) {
            val boards = databaseBoard.getBoards()
            boards
        }
    }

    private fun getBoardsFromAPI() {
        coroutineScope.launch {
            val getPresetsDeferred = MyApiIOT.retrofitService.getBoardsById(userID)
            try {
                val listResult = getPresetsDeferred.await()
                _boards.value = listResult
            }catch (e: Exception) {
                Log.i("API ERROR -- Boards", "Exception with API -- Using local DB")
                _boards.value = getBoardsFromDatabase()
            }
        }
    }

    //Other functions
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
        Log.i("HomeViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
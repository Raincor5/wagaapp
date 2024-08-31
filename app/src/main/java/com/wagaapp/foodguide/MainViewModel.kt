package com.wagaapp.foodguide

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.logging.Logger

class MainViewModel(application: Application, val sharedPreferences: SharedPreferences) : AndroidViewModel(application) {
    // TODO: Change the sessionKey to a more secure value and remove hardcoded value
    private val sessionKey = "session_start_time"

    private val _isSessionActive = MutableStateFlow(false)
    val isSessionActive: StateFlow<Boolean> = _isSessionActive

    private val _usedIngredients = MutableStateFlow<Set<String>>(emptySet())
    val usedIngredients: StateFlow<Set<String>> = _usedIngredients

    private val logger = Logger.getLogger(MainViewModel::class.java.name)

    init {
        viewModelScope.launch {
            checkSession()
        }
    }

    internal fun checkSession() {
        val lastSessionTime = sharedPreferences.getLong(sessionKey, 0L)
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val nextSessionTime = calendar.timeInMillis

        logger.info("Last session time: $lastSessionTime")
        logger.info("Current time: $currentTime")
        logger.info("Next session time: $nextSessionTime")

        if (currentTime >= nextSessionTime && lastSessionTime < nextSessionTime) {
            startNewSession()
        } else {
            _isSessionActive.value = true
        }

    }


    private fun startNewSession() {
        if (_isSessionActive.value) return
        val currentTime = System.currentTimeMillis()
        sharedPreferences.edit().putLong(sessionKey, currentTime).apply()
        _isSessionActive.value = true
    }


    fun restartSessionManually() {
        // If the session is already active, cancel it and start a new one
        if (_isSessionActive.value) {
            cancelSession()
        }
        startNewSession()
    }

    fun cancelSession() {
        _isSessionActive.value = false
        _usedIngredients.value = emptySet()
    }

    fun toggleIngredientUsed(ingredientKey: String) {
        viewModelScope.launch {
            val currentSet = _usedIngredients.value.toMutableSet()
            if (currentSet.contains(ingredientKey)) {
                currentSet.remove(ingredientKey)
            } else {
                currentSet.add(ingredientKey)
            }
            _usedIngredients.value = currentSet
        }
    }
}
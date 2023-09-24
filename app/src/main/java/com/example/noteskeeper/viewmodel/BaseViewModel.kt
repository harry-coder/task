package com.think.searchimage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

open class BaseViewModel : ViewModel() {

    fun <T : Any> ioThenMain( work: suspend (() -> T?),  callback: ((T?) -> Unit)? ): Job =
        viewModelScope.launch(Dispatchers.Main) {
            val data = viewModelScope.async(Dispatchers.IO) {
                return@async work()
            }.await()
            callback?.let {
                it(data)
            }
        }


    fun handlerWithDelay(includeDelay: Boolean = false, delay: Long = 0, work: () -> Unit): Job =
        viewModelScope.launch(Dispatchers.Main) {
            if (includeDelay) {
                delay(delay)
                work()
            } else work()
        }

     fun startCoroutineTimer(delayMillis: Long = 0, initialDelay: Long = 0,  action: () -> Unit) = viewModelScope.launch {
        delay(initialDelay)
            while (true) {
                action()
                delay(delayMillis)
            }
        }
    }

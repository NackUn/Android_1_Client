package com.yapp.picon.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yapp.picon.data.model.Address
import com.yapp.picon.data.model.Coordinate
import com.yapp.picon.data.model.Post
import com.yapp.picon.data.network.NetworkModule
import com.yapp.picon.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class MapViewModel : BaseViewModel() {
    private val _isButtonShown = MutableLiveData<Boolean>()
    val isButtonShown: LiveData<Boolean> get() = _isButtonShown

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> get() = _toastMsg

    init {
        _isButtonShown.value = false
    }

    private fun showToast(msg: String) {
        _toastMsg.value = msg
    }

    fun toggleButtonShown() {
        _isButtonShown.value = _isButtonShown.value?.let {
            !it
        }
    }

    fun requestPost() {
        viewModelScope.launch {
            try {
                NetworkModule.yappApi.requestPost("1").let {
                    showToast("감정 : ${it.emotion} 메모 : ${it.memo}")
                }
            } catch (e: Exception) {
                showToast("통신 오류")
            }
        }
    }

    fun createPost() {
        viewModelScope.launch {
            try {
                NetworkModule.yappApi.createPost(
                    Post(
                        10,
                        Coordinate(
                            1.5,
                            1.5
                        ),
                        Address(
                            "null",
                            "null",
                            "null",
                            "null"
                        ),
                        "1",
                        "1",
                        null
                    )
                ).let {
                    showToast(it.toString())
                }
            } catch (e: Exception) {
                showToast("통신 오류")
            }
        }
    }
}
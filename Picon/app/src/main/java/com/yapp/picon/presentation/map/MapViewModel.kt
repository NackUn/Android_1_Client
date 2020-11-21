package com.yapp.picon.presentation.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yapp.picon.domain.usecase.LoadAccessTokenUseCase
import com.yapp.picon.domain.usecase.LogoutUseCase
import com.yapp.picon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MapViewModel(
    private val loadAccessTokenUseCase: LoadAccessTokenUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel() {
    private val _isButtonShown = MutableLiveData<Boolean>()
    val isButtonShown: LiveData<Boolean> get() = _isButtonShown

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> get() = _toastMsg

    private val _isShownPostForm = MutableLiveData<Boolean>()
    val isShownPostForm: LiveData<Boolean> get() = _isShownPostForm

    private val _isShownPostFormBtn = MutableLiveData<Boolean>()
    val isShownPostFormBtn: LiveData<Boolean> get() = _isShownPostFormBtn

    init {
        _isButtonShown.value = false
        _isShownPostForm.value = false
        _isShownPostFormBtn.value = false
    }

    private fun showToast(msg: String) {
        _toastMsg.value = msg
    }

    fun toggleButtonShown() {
        _isButtonShown.value = _isButtonShown.value?.let {
            !it
        }
    }

    fun toggleShowPostForm() {
        _isShownPostForm.value = _isShownPostForm.value?.let {
            !it
        }
    }

    fun toggleShowPostFormBtn() {
        _isShownPostFormBtn.value = _isShownPostFormBtn.value?.let {
            !it
        }
    }

    fun loadAccessToken() {
        viewModelScope.launch {
            loadAccessTokenUseCase().collect {
                Log.e("aa12", it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

//    fun requestPost() {
//        viewModelScope.launch {
//            try {
//                NetworkModule.yappApi.requestPost("1").let {
//                    showToast("감정 : ${it.emotion} 메모 : ${it.memo}")
//                }
//            } catch (e: Exception) {
//                showToast("통신 오류")
//            }
//        }
//    }

//    fun createPost() {
//        viewModelScope.launch {
//            try {
//                NetworkModule.yappApi.createPost(
//                    Post(
//                        10,
//                        Coordinate(
//                            1.5,
//                            1.5
//                        ),
//                        Address(
//                            "null",
//                            "null",
//                            "null",
//                            "null"
//                        ),
//                        "1",
//                        "1",
//                        null
//                    )
//                ).let {
//                    showToast(it.toString())
//                }
//            } catch (e: Exception) {
//                showToast("통신 오류")
//            }
//        }
//    }
}
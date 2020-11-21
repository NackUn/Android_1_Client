package com.yapp.picon.presentation.nav

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yapp.picon.presentation.base.BaseViewModel
import com.yapp.picon.presentation.nav.repository.CustomEmotionRepository
import com.yapp.picon.presentation.nav.repository.SettingRepository

class NavViewModel : BaseViewModel() {
    val settingRepository = SettingRepository()
    val customRepository = CustomEmotionRepository()

    private val _finishFlag = MutableLiveData<Boolean>()

    val finishFlag: LiveData<Boolean> get() = _finishFlag

    init {
        _finishFlag.value = false
    }

    fun clickFinishButton(view: View) {
        changeFinishFlag()
    }

    fun changeFinishFlag() {
        _finishFlag.value = _finishFlag.value?.let {
            !it
        }
    }

    fun clickCustomFinishButton(view: View) {
        customRepository.changeCustomFinishFlag()
    }

    fun clickCustomSaveButton(view: View) {
        customRepository.changeCustomSaveFlag()
        _finishFlag.value = _finishFlag.value?.let {
            !it
        }
    }

    fun settingInitializeDialogFlag() {
        settingRepository.initializeSettingFlag()
    }
}
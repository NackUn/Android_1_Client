package com.yapp.picon.presentation.nav

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.yapp.picon.BR
import com.yapp.picon.R
import com.yapp.picon.databinding.DialogCustomFinishBinding
import com.yapp.picon.databinding.NavCustomEmotionFragmentBinding
import com.yapp.picon.presentation.base.BaseFragment

class CustomEmotionFragment : BaseFragment<NavCustomEmotionFragmentBinding, NavViewModel>(
    R.layout.nav_custom_emotion_fragment
) {
    private lateinit var customAdapter: CustomEmotionAdapter
    private lateinit var finishDialog: Dialog
    private lateinit var finishBuilder: AlertDialog.Builder

    @Suppress("UNCHECKED_CAST")
    override val vm: NavViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                NavViewModel() as T
        }
    }

    override fun initBinding() {
        binding.setVariable(BR.navVM, vm)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()

        setFinishDialog()

        observeFinishButton()
        observeFinishDialogCancelButton()
        observeFinishDialogConfirmButton()
        observeSaveButton()

        setRecyclerView()
    }

    @SuppressLint("InflateParams")
    private fun setFinishDialog() {
        val dialogFinishView: DialogCustomFinishBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_custom_finish, null, false
        )
        dialogFinishView.setVariable(BR.customRepo, vm.customRepository)
        finishBuilder = AlertDialog.Builder(context)
        finishBuilder.setView(dialogFinishView.root)

        finishDialog = finishBuilder.create()
        finishDialog.setCancelable(false)
        finishDialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun observeFinishButton() {
        vm.customRepository.customFinishFlag.observe(this) {
            if (it) {
                finishDialog.show()
                setDialogSize()
            }
        }
    }

    private fun observeFinishDialogCancelButton() {
        vm.customRepository.dialogFinishCancelFlag.observe(this) {
            if (it) {
                finishDialog.dismiss()
                vm.customRepository.initFinishDialogFlag()
                vm.customRepository.changeCustomFinishFlag()
            }
        }
    }

    private fun observeFinishDialogConfirmButton() {
        vm.customRepository.dialogFinishConfirmFlag.observe(this) {
            if (it) {
                finishDialog.dismiss()
                vm.changeFinishFlag()
            }
        }
    }

    private fun observeSaveButton() {
        vm.customRepository.customSaveFlag.observe(this) {
            if (it) {
                // TODO("custom repository의 items를 서버에 저장")
                Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDialogSize() {
        val layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(finishDialog.window!!.attributes)
            width = (274 * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        finishDialog.window?.attributes = layoutParams
    }

    private fun setRecyclerView() {
        customAdapter = CustomEmotionAdapter(
            vm.customRepository.items.value ?: listOf()
        ) { index: Int, value: String ->
            vm.customRepository.setItems(index, value)
        }

        binding.navEmotionRecyclerView.apply {
            adapter = customAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }
}
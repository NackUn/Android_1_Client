package com.yapp.picon.presentation.nav

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.yapp.picon.BR
import com.yapp.picon.R
import com.yapp.picon.databinding.DialogRemoveAllDataBinding
import com.yapp.picon.databinding.NavSettingFragmentBinding
import com.yapp.picon.presentation.base.BaseFragment
import com.yapp.picon.presentation.nav.repository.SettingRepository

class SettingFragment : BaseFragment<NavSettingFragmentBinding, NavViewModel>(
    R.layout.nav_setting_fragment
) {
    private lateinit var dialog: AlertDialog
    private lateinit var repo: SettingRepository

    @Suppress("UNCHECKED_CAST")
    override val vm: NavViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                NavViewModel() as T
        }
    }

    override fun initBinding() {
        binding.setVariable(BR.navVM, vm)
        binding.setVariable(BR.settingRepo, vm.settingRepository)
    }

    override fun onStart() {
        super.onStart()
        repo = vm.settingRepository
        setReviewButton()
        setRemoveDataDialog()
    }

    private fun setReviewButton() {
        repo.reviewFlag.observe(this, {
            if (it) {
                // TODO("앱 배포 이후에 아래 주석을 해제하면 됩니다.")
                // startAppStore()
            }
        })
    }

    private fun startAppStore() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                "https://play.google.com/store/apps/details?id=com.yapp.picon"
            )
            setPackage("com.android.vending")
        }
        startActivity(intent)
    }

    private fun setRemoveDataDialog() {
        val dialogBinding: DialogRemoveAllDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_remove_all_data,
            null,
            false
        )
        dialogBinding.setVariable(BR.settingRepo, repo)
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogBinding.root)
        dialog = builder.create()

        dialog.setCancelable(false)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        observeRemoveAllData()
        observeDialogCancel()
        observeDialogRemove()
    }

    private fun observeRemoveAllData() {
        repo.removeAllDataFlag.observe(this, {
            if (it) {
                dialog.show()
                setDialogSize()
            }
        })
    }

    private fun setDialogSize() {
        val layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(dialog.window!!.attributes)
            width = (274 * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        dialog.window?.attributes = layoutParams
    }

    private fun observeDialogCancel() {
        repo.dialogDismissFlag.observe(this, {
            if (it) {
                dialog.dismiss()
                vm.settingInitializeDialogFlag()
            }
        })
    }

    private fun observeDialogRemove() {
        repo.dialogRemoveFlag.observe(this, {
            if (it) {
                // TODO("모든 데이터 삭제 - 비동기")
                Toast.makeText(context, "모든 데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                vm.settingInitializeDialogFlag()
            }
        })
    }
}
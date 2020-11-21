package com.yapp.picon.presentation.base

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.yapp.picon.R

abstract class BaseActivity<T : ViewDataBinding, VM : ViewModel>(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity() {

    protected lateinit var binding: T

    abstract val vm: VM

    abstract fun initViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

        initViewModel()
    }

    protected open fun showToast(msg: String) {
        val customToast =
            layoutInflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast))
        customToast.findViewById<TextView>(R.id.custom_toast_tv).text = msg
        Toast(this).run {
            setGravity(Gravity.BOTTOM, 0, 200)
            duration = Toast.LENGTH_SHORT
            view = customToast
            show()
        }
    }

}
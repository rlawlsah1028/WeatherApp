package com.bp.weatherapp.views.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bp.weatherapp.R

/**
 * BaseActivity. 공통으로 사용할 코드 정의.
 * */
open class BaseActivity : AppCompatActivity() {


    val loadingDialog: Dialog by lazy {
        Dialog(this)?.let { dialog ->
            dialog.setContentView(this.layoutInflater.inflate(R.layout.dialog_loading, null))
            dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog
        }
    }

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes id: Int): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, id).also {
            it.lifecycleOwner = this
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun showLoading() {
        this.loadingDialog.show()

    }

    fun hideLoading() {

        if (this.loadingDialog.isShowing) {
            this.loadingDialog.cancel()
        }
    }

}
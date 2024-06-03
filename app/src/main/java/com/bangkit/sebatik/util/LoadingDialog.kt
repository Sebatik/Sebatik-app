package com.bangkit.sebatik.util

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.bangkit.sebatik.R

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading_dialog)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    fun showLoading() {
        if (!isShowing) {
            show()
        }
    }

    fun hideLoading() {
        if (isShowing) {
            dismiss()
        }
    }
}

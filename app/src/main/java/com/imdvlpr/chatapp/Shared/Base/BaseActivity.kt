package com.imdvlpr.chatapp.Shared.Base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import com.imdvlpr.chatapp.R

open class BaseActivity : AppCompatActivity() {

    private var dialogProgress: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initProgress()
    }

    private fun initProgress() {
        // custom dialog
        dialogProgress = Dialog(this)
        dialogProgress?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogProgress?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogProgress?.setContentView(R.layout.dialog_progress)
        dialogProgress?.setCancelable(false)
    }

    fun showProgress() {
        if (!isFinishing && dialogProgress?.isShowing == false && dialogProgress != null)
            dialogProgress?.show()
    }
    fun hideProgress() { if (!isFinishing && dialogProgress?.isShowing == true) dialogProgress?.dismiss() }
}
package com.mvvm.okcredit.ui.splash

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import com.mvvm.okcredit.R
import com.mvvm.okcredit.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)


        postDelay()
    }
    private fun postDelay() {
        showProgressDialog()
        Handler(Looper.getMainLooper()).postDelayed({
            val  intent = Intent(baseContext,HomeActivity::class.java)
            startActivity(intent)
            dismissProgressDialog()
        }, 3000)
    }
    private lateinit var mDialog: Dialog
    private fun showProgressDialog() {
        mDialog = Dialog(this, R.style.MaterialDialogSheet)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.progress_loader)
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }

    fun dismissProgressDialog() {
        if (mDialog.isShowing) {
            mDialog.dismiss()
        }
    }

}
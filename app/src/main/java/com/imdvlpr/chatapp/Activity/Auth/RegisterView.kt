package com.imdvlpr.chatapp.Activity.Auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.UI.InputView
import com.imdvlpr.chatapp.databinding.ActivityRegisterBinding

class RegisterView : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent{
            return Intent(context, RegisterView::class.java)
        }
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        binding.txtName.apply {
            setTitle(getString(R.string.register_full_name))
            setHint(getString(R.string.register_full_name_hint))
            setVisibleIcon(true, R.drawable.ic_user)
            setInputType(InputView.TYPE.TEXT)
            setImeOption(InputView.ACTION.DONE)
        }

        binding.txtEmail.apply {
            setTitle(getString(R.string.register_email))
            setHint(getString(R.string.register_email_hint))
            setVisibleIcon(true, R.drawable.ic_email)
            setInputType(InputView.TYPE.EMAIL)
            setImeOption(InputView.ACTION.DONE)
        }

        binding.txtPassword.apply {
            setTitle(getString(R.string.register_password))
            setHint(getString(R.string.register_password_hint))
            setVisibleIcon(true, R.drawable.ic_lock)
            setInputType(InputView.TYPE.PASSWORD)
            setImeOption(InputView.ACTION.DONE)
        }

        binding.txtConfirmPassword.apply {
            setTitle(getString(R.string.register_confirm_password))
            setHint(getString(R.string.register_password_hint))
            setVisibleIcon(true, R.drawable.ic_lock)
            setInputType(InputView.TYPE.PASSWORD)
            setImeOption(InputView.ACTION.DONE)
        }
    }
}